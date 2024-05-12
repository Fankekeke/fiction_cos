package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.FollowInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IFollowInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/follow-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FollowInfoController {

    private final IFollowInfoService followInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取用户关注信息
     *
     * @param page       分页对象
     * @param followInfo 用户关注信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<FollowInfo> page, FollowInfo followInfo) {
        return R.ok(followInfoService.selectFollowPage(page, followInfo));
    }

    /**
     * 用户关注信息详情
     *
     * @param id 用户关注ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(followInfoService.selectDetail(id));
    }

    /**
     * 用户关注信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(followInfoService.list());
    }

    /**
     * 新增用户关注信息
     *
     * @param followInfo 用户关注信息
     * @return 结果
     */
    @PostMapping
    public R save(FollowInfo followInfo) {
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, followInfo.getUserId()));
        if (userInfo != null) {
            followInfo.setUserId(userInfo.getId());
        }
        // 判断用户是否关注
        int count = followInfoService.count(Wrappers.<FollowInfo>lambdaQuery().eq(FollowInfo::getAuthorId, followInfo.getAuthorId()).eq(FollowInfo::getUserId, userInfo.getId()));
        if (count > 0) {
            return R.ok(true);
        } else {
            followInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            return R.ok(followInfoService.save(followInfo));
        }
    }

    /**
     * 修改用户关注信息
     *
     * @param followInfo 用户关注信息
     * @return 结果
     */
    @PutMapping
    public R edit(FollowInfo followInfo) {
        return R.ok(followInfoService.updateById(followInfo));
    }

    /**
     * 删除用户关注信息
     *
     * @param ids ids
     * @return 用户关注信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(followInfoService.removeByIds(ids));
    }
}
