package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.utils.ItemCF;
import cc.mrbird.febs.cos.dao.AuthorInfoMapper;
import cc.mrbird.febs.cos.dao.BookDetailInfoMapper;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.BookInfoMapper;
import cc.mrbird.febs.cos.service.IBookInfoService;
import cc.mrbird.febs.cos.service.IBookLikeInfoService;
import cc.mrbird.febs.cos.service.IEvaluateInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo> implements IBookInfoService {

    private final AuthorInfoMapper authorInfoMapper;

    private final BookDetailInfoMapper bookDetailInfoMapper;

    private final IEvaluateInfoService evaluateInfoService;

    private final IBookLikeInfoService bookLikeInfoService;

    private final UserInfoMapper userInfoMapper;

    /**
     * 分页获取书籍信息
     *
     * @param page     分页对象
     * @param bookInfo 书籍信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectBookPage(Page<BookInfo> page, BookInfo bookInfo) {
        return baseMapper.selectBookPage(page, bookInfo);
    }

    /**
     * 推荐列表
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public List<BookInfo> userCfRecommend(Integer userId) {
        // 获取所有评论信息
        List<EvaluateInfo> evaluateInfoList = evaluateInfoService.list();
        // 根据用户ID与图书ID转map
        Map<String, List<EvaluateInfo>> evaluateInfoMap = evaluateInfoList.stream().collect(Collectors.groupingBy(e -> e.getUserId() + "|" + e.getBookId()));
        // 获取所有点赞信息
        List<BookLikeInfo> bookLikeInfoList = bookLikeInfoService.list();
        // 根据用户ID与图书ID转map
        Map<String, List<BookLikeInfo>> bookLikeInfoMap = bookLikeInfoList.stream().collect(Collectors.groupingBy(e -> e.getUserId() + "|" + e.getBookId()));

        // 所有图书信息
        List<BookInfo> bookInfoList = this.list();
        // 所有用户信息
        List<UserInfo> userInfoList = userInfoMapper.selectList(Wrappers.<UserInfo>lambdaQuery());

        if (CollectionUtil.isEmpty(bookInfoList) || CollectionUtil.isEmpty(userInfoList)) {
            return Collections.emptyList();
        }

        List<RelateDTO> data= new ArrayList<>();
        // 评论一次+5，点赞一次+10
        for (BookInfo bookInfo : bookInfoList) {
            for (UserInfo userInfo : userInfoList) {
                // 获取此用户对应的评论次数
                List<EvaluateInfo> evaluateInfos = evaluateInfoMap.get(userInfo.getId() + "|" + bookInfo.getId());
                // 获取此用户对应的点赞次数
                List<BookLikeInfo> bookLikeInfos = bookLikeInfoMap.get(userInfo.getId() + "|" + bookInfo.getId());
                if (CollectionUtil.isNotEmpty(evaluateInfos) || CollectionUtil.isNotEmpty(bookLikeInfos)) {
                    // 获取此用户对应的评论次数
                    int evaluateCount = CollectionUtil.isEmpty(evaluateInfos) ? 0 : evaluateInfos.size();
                    // 获取此用户对应的点赞次数
                    int likeCount = CollectionUtil.isEmpty(bookLikeInfos) ? 0 : bookLikeInfos.size();
                    // 评论一次+5，点赞一次+10
                    data.add(new RelateDTO(userInfo.getId(), bookInfo.getId(), NumberUtil.add(NumberUtil.mul(evaluateCount, 5), NumberUtil.mul(likeCount, 10))));
                } else {
                    data.add(new RelateDTO(userInfo.getId(), bookInfo.getId(), 0.0));
                }
            }
        }
        // 获取到推荐的id
        List<Integer> recommendations = ItemCF.recommend(userId, data);
        if (CollectionUtil.isEmpty(recommendations)) {
            return Collections.emptyList();
        }

        // 查询所有图书信息
        List<BookInfo> bookInfoListResult = this.list(Wrappers.<BookInfo>lambdaQuery().in(BookInfo::getId, recommendations));
        if (CollectionUtil.isEmpty(bookInfoListResult)) {
            return bookInfoListResult;
        }

        // 获取所有作者
        List<AuthorInfo> authorInfoList = authorInfoMapper.selectList(Wrappers.<AuthorInfo>lambdaQuery());
        Map<Integer, AuthorInfo> authorInfoMap = authorInfoList.stream().collect(Collectors.toMap(AuthorInfo::getId, e -> e));

        for (BookInfo bookInfo : bookInfoListResult) {
            if (CollectionUtil.isNotEmpty(authorInfoMap) && authorInfoMap.get(bookInfo.getAuthorId()) != null) {
                bookInfo.setAuthorInfo(authorInfoMap.get(bookInfo.getAuthorId()));
            }
        }
        // 按照recommendations排序
        bookInfoListResult.sort(Comparator.comparingInt(bookInfo -> recommendations.indexOf(bookInfo.getId())));
        return bookInfoListResult;
    }

    /**
     * 书籍信息详情
     *
     * @param bookId 书籍ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectDetailById(Integer bookId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("book", null);
                put("detail", Collections.emptyList());
                put("author", null);
            }
        };

        // 图书信息
        BookInfo bookInfo = this.getById(bookId);
        result.put("book", bookInfo);
        // 图书章节
        List<BookDetailInfo> detailList = bookDetailInfoMapper.selectList(Wrappers.<BookDetailInfo>lambdaQuery().eq(BookDetailInfo::getBookId, bookId).orderByDesc(BookDetailInfo::getIndexNo));
        result.put("detail", detailList);
        // 作者信息
        AuthorInfo authorInfo = authorInfoMapper.selectById(bookInfo.getAuthorId());
        result.put("author", authorInfo);

        return result;
    }

    /**
     * 根据用户ID获取书籍信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public List<BookInfo> selectBookByUserId(Integer userId) {
        // 获取作者信息
        AuthorInfo authorInfo = authorInfoMapper.selectOne(Wrappers.<AuthorInfo>lambdaQuery().eq(AuthorInfo::getUserId, userId));

        if (authorInfo == null) {
            return Collections.emptyList();
        }

        return this.list(Wrappers.<BookInfo>lambdaQuery().eq(BookInfo::getAuthorId, authorInfo.getId()));
    }

    /**
     * 文章统计列表
     *
     * @return 结果
     */
    @Override
    public List<BookInfo> selectListDetail() {
        // 查询所有图书信息
        List<BookInfo> bookInfoList = this.list();
        if (CollectionUtil.isEmpty(bookInfoList)) {
            return bookInfoList;
        }

        // 获取所有作者
        List<AuthorInfo> authorInfoList = authorInfoMapper.selectList(Wrappers.<AuthorInfo>lambdaQuery());
        Map<Integer, AuthorInfo> authorInfoMap = authorInfoList.stream().collect(Collectors.toMap(AuthorInfo::getId, e -> e));

        for (BookInfo bookInfo : bookInfoList) {
            if (CollectionUtil.isNotEmpty(authorInfoMap) && authorInfoMap.get(bookInfo.getAuthorId()) != null) {
                bookInfo.setAuthorInfo(authorInfoMap.get(bookInfo.getAuthorId()));
            }
        }
        return bookInfoList;
    }

    /**
     * 搜索
     *
     * @param key 关键字
     * @return 结果
     */
    @Override
    public List<BookInfo> selectListBySearch(String key) {
        // 查询所有图书信息
        List<BookInfo> bookInfoList = this.list();
        if (CollectionUtil.isEmpty(bookInfoList)) {
            return bookInfoList;
        }

        // 获取所有作者
        List<AuthorInfo> authorInfoList = authorInfoMapper.selectList(Wrappers.<AuthorInfo>lambdaQuery());
        Map<Integer, AuthorInfo> authorInfoMap = authorInfoList.stream().collect(Collectors.toMap(AuthorInfo::getId, e -> e));

        for (BookInfo bookInfo : bookInfoList) {
            if (CollectionUtil.isNotEmpty(authorInfoMap) && authorInfoMap.get(bookInfo.getAuthorId()) != null) {
                bookInfo.setAuthorInfo(authorInfoMap.get(bookInfo.getAuthorId()));
            }
        }
        if (CollectionUtil.isEmpty(bookInfoList)) {
            return Collections.emptyList();
        }

        // 根据书名和作者过滤
        bookInfoList = bookInfoList.stream().filter(e -> e.getName().contains(key) || e.getAuthorInfo().getName().contains(key)).collect(Collectors.toList());
        return bookInfoList;
    }

    /**
     * 文章流量卡排行列表
     *
     * @return 结果
     */
    @Override
    public List<BookInfo> selectListTop() {
        // 查询所有图书信息
        List<BookInfo> bookInfoList = this.selectListDetail();
        if (CollectionUtil.isEmpty(bookInfoList)) {
            return bookInfoList;
        }

        List<BookDetailInfo> bookDetailList = bookDetailInfoMapper.selectList(Wrappers.<BookDetailInfo>lambdaQuery());
        Map<Integer, List<BookDetailInfo>> bookDetailMap = bookDetailList.stream().collect(Collectors.groupingBy(BookDetailInfo::getBookId));

        for (BookInfo bookInfo : bookInfoList) {
            if (CollectionUtil.isNotEmpty(bookDetailMap) && CollectionUtil.isNotEmpty(bookDetailMap.get(bookInfo.getId()))) {
                List<BookDetailInfo> detailInfoList = bookDetailMap.get(bookInfo.getId());
                Integer count = detailInfoList.stream().mapToInt(BookDetailInfo::getViews).sum();
                bookInfo.setViews(count);
            } else {
                bookInfo.setViews(0);
            }
        }

        // 排序
        return bookInfoList.stream().sorted(Comparator.comparing(BookInfo::getViews).reversed()).collect(Collectors.toList());
    }
}
