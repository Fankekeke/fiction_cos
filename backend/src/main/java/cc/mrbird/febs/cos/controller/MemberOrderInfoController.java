package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MemberOrderInfo;
import cc.mrbird.febs.cos.service.IMemberOrderInfoService;
import cn.hutool.core.date.DateUtil;
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
@RequestMapping("/cos/member-order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberOrderInfoController {

    private final IMemberOrderInfoService memberOrderInfoService;

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
