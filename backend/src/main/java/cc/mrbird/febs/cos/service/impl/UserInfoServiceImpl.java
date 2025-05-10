package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.AuthorInfoMapper;
import cc.mrbird.febs.cos.dao.FollowInfoMapper;
import cc.mrbird.febs.cos.dao.MemberInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.service.IBookInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    private final MemberInfoMapper memberInfoMapper;

    private final FollowInfoMapper followInfoMapper;

    private final AuthorInfoMapper authorInfoMapper;

    private final IBookInfoService bookInfoService;

    /**
     * 分页获取用户信息
     *
     * @param page     分页对象
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectUserPage(Page<UserInfo> page, UserInfo userInfo) {
        return baseMapper.selectUserPage(page, userInfo);
    }

    /**
     * 用户信息详情
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> userDetail(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        UserInfo userInfo = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        result.put("user", userInfo);
        // 获取用户关注作者
        List<FollowInfo> followInfoList = followInfoMapper.selectList(Wrappers.<FollowInfo>lambdaQuery().eq(FollowInfo::getUserId, userInfo.getId()));
        List<Integer> authorIdList = followInfoList.stream().map(FollowInfo::getAuthorId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(authorIdList)) {
            result.put("order", Collections.emptyList());
        } else {
            // 获取作者图书信息
            List<BookInfo> bookInfoList = bookInfoService.list(Wrappers.<BookInfo>lambdaQuery().in(BookInfo::getAuthorId, authorIdList));
            // 获取用户关注作者的更新时间
            result.put("order", bookInfoList);
        }
        return result;
    }
}
