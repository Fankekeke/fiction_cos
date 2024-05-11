package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.BookInfoMapper;
import cc.mrbird.febs.cos.dao.FollowInfoMapper;
import cc.mrbird.febs.cos.entity.AuthorInfo;
import cc.mrbird.febs.cos.dao.AuthorInfoMapper;
import cc.mrbird.febs.cos.entity.BookInfo;
import cc.mrbird.febs.cos.entity.FollowInfo;
import cc.mrbird.febs.cos.service.IAuthorInfoService;
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
public class AuthorInfoServiceImpl extends ServiceImpl<AuthorInfoMapper, AuthorInfo> implements IAuthorInfoService {

    private final FollowInfoMapper followInfoMapper;

    private final BookInfoMapper bookInfoMapper;

    /**
     * 分页获取作家信息
     *
     * @param page       分页对象
     * @param authorInfo 作家信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectAuthorPage(Page<AuthorInfo> page, AuthorInfo authorInfo) {
        return baseMapper.selectAuthorPage(page, authorInfo);
    }

    /**
     * 作家信息详情
     *
     * @param id 作家ID
     * @return 结果
     */
    @Override
    public AuthorInfo selectAuthorDetail(Integer id) {
        AuthorInfo authorInfo = this.getById(id);
        // 粉丝数量
        Integer count = followInfoMapper.selectCount(Wrappers.<FollowInfo>lambdaQuery().eq(FollowInfo::getAuthorId, id));
        authorInfo.setFansNum(count);

        // 图书信息
        List<BookInfo> bookInfoList = bookInfoMapper.selectList(Wrappers.<BookInfo>lambdaQuery().eq(BookInfo::getAuthorId, id));
        authorInfo.setBookInfoList(bookInfoList);
        return authorInfo;
    }

    /**
     * 获取作者信息
     *
     * @return 结果
     */
    @Override
    public List<AuthorInfo> selectTopAuthor() {
        // 获取所有作者信息
        List<AuthorInfo> authorInfoList = this.list();
        if (CollectionUtil.isEmpty(authorInfoList)) {
            return authorInfoList;
        }

        List<FollowInfo> followInfos = followInfoMapper.selectList(Wrappers.<FollowInfo>lambdaQuery());
        Map<Integer, List<FollowInfo>> followInfoMap = followInfos.stream().collect(Collectors.groupingBy(FollowInfo::getAuthorId));

        List<BookInfo> bookInfoList = bookInfoMapper.selectList(Wrappers.<BookInfo>lambdaQuery());
        Map<Integer, List<BookInfo>> bookInfoMap = bookInfoList.stream().collect(Collectors.groupingBy(BookInfo::getAuthorId));

        for (AuthorInfo authorInfo : authorInfoList) {
            if (CollectionUtil.isNotEmpty(followInfoMap) && CollectionUtil.isNotEmpty(followInfoMap.get(authorInfo.getId()))) {
                authorInfo.setFansNum(followInfoMap.get(authorInfo.getId()).size());
            } else {
                authorInfo.setFansNum(0);
            }

            if (CollectionUtil.isNotEmpty(bookInfoMap) && CollectionUtil.isNotEmpty(bookInfoMap.get(authorInfo.getId()))) {
                authorInfo.setBookInfoList(bookInfoMap.get(authorInfo.getId()));
            } else {
                authorInfo.setBookInfoList(Collections.emptyList());
            }
        }
        return authorInfoList;
    }

    /**
     * 热门创作者统计
     *
     * @return 结果
     */
    @Override
    public List<AuthorInfo> selectListDetail() {
        List<AuthorInfo> authorInfoList = this.selectTopAuthor();
        return authorInfoList.stream().sorted(Comparator.comparing(AuthorInfo::getFansNum).reversed()).collect(Collectors.toList());
    }
}
