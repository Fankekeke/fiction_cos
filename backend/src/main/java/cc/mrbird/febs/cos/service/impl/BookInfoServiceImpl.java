package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.AuthorInfoMapper;
import cc.mrbird.febs.cos.dao.BookDetailInfoMapper;
import cc.mrbird.febs.cos.entity.AuthorInfo;
import cc.mrbird.febs.cos.entity.BookDetailInfo;
import cc.mrbird.febs.cos.entity.BookInfo;
import cc.mrbird.febs.cos.dao.BookInfoMapper;
import cc.mrbird.febs.cos.service.IBookInfoService;
import cn.hutool.core.collection.CollectionUtil;
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
