package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.IMemberInfoService;
import cc.mrbird.febs.cos.service.IMemberOrderInfoService;
import cc.mrbird.febs.cos.service.IRuleInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/member-order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberOrderInfoController {

    private final IMemberOrderInfoService memberOrderInfoService;

    private final IUserInfoService userInfoService;

    private final IMemberInfoService memberInfoService;

    private final IRuleInfoService ruleInfoService;

    /**
     * 分页获取会员订单信息
     *
     * @param page            分页对象
     * @param memberOrderInfo 会员订单信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<MemberOrderInfo> page, MemberOrderInfo memberOrderInfo) {
        return R.ok(memberOrderInfoService.selectOrderByPage(page, memberOrderInfo));
    }

    /**
     * 根据用户ID获取会员信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/member/{userId}")
    public R selectMemberByUserId(@PathVariable("userId") Integer userId) {
        // 返回信息
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        result.put("user", userInfo);

        // 会员信息
        List<MemberInfo> memberInfos = memberInfoService.list(Wrappers.<MemberInfo>lambdaQuery().eq(MemberInfo::getUserId, userInfo.getId()));
        if (CollectionUtil.isNotEmpty(memberInfos)) {
            for (MemberInfo memberInfo : memberInfos) {
                if (DateUtil.isIn(new Date(), DateUtil.parseDateTime(memberInfo.getStartDate()), DateUtil.parseDateTime(memberInfo.getEndDate()))) {
                    RuleInfo ruleInfo = ruleInfoService.getById(memberInfo.getMemberLevel());
                    memberInfo.setRuleName(ruleInfo.getName());
                    result.put("member", memberInfo);
                    return R.ok(result);
                }
            }
        } else {
            result.put("member", null);
        }
        return R.ok(result);
    }

    /**
     * 首页数据统计
     *
     * @return 结果
     */
    @GetMapping("/home/data")
    public R homeData() {
        return R.ok(memberOrderInfoService.homeData());
    }

    /**
     * 会员订单信息详情
     *
     * @param id 会员订单ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(memberOrderInfoService.getById(id));
    }

    /**
     * 会员订单信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(memberOrderInfoService.list());
    }

    /**
     * 新增会员订单信息
     *
     * @param memberOrderInfo 会员订单信息
     * @return 结果
     */
    @PostMapping
    public R save(MemberOrderInfo memberOrderInfo) {
        memberOrderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(memberOrderInfoService.save(memberOrderInfo));
    }

    /**
     * 修改会员订单信息
     *
     * @param memberOrderInfo 会员订单信息
     * @return 结果
     */
    @PutMapping
    public R edit(MemberOrderInfo memberOrderInfo) {
        return R.ok(memberOrderInfoService.updateById(memberOrderInfo));
    }

    /**
     * 删除会员订单信息
     *
     * @param ids ids
     * @return 会员订单信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(memberOrderInfoService.removeByIds(ids));
    }
}
