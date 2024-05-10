package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.EvaluateInfo;
import cc.mrbird.febs.cos.service.IEvaluateInfoService;
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
@RequestMapping("/cos/evaluate-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EvaluateInfoController {

    private final IEvaluateInfoService evaluateInfoService;

    /**
     * 分页获取书籍评价信息
     *
     * @param page         分页对象
     * @param evaluateInfo 书籍评价信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<EvaluateInfo> page, EvaluateInfo evaluateInfo) {
        return R.ok(evaluateInfoService.selectEvaluatePage(page, evaluateInfo));
    }

    /**
     * 书籍评价信息详情
     *
     * @param id 书籍评价ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(evaluateInfoService.getById(id));
    }

    /**
     * 书籍评价信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(evaluateInfoService.list());
    }

    /**
     * 新增书籍评价信息
     *
     * @param evaluateInfo 书籍评价信息
     * @return 结果
     */
    @PostMapping
    public R save(EvaluateInfo evaluateInfo) {
        evaluateInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(evaluateInfoService.save(evaluateInfo));
    }

    /**
     * 修改书籍评价信息
     *
     * @param evaluateInfo 书籍评价信息
     * @return 结果
     */
    @PutMapping
    public R edit(EvaluateInfo evaluateInfo) {
        return R.ok(evaluateInfoService.updateById(evaluateInfo));
    }

    /**
     * 删除书籍评价信息
     *
     * @param ids ids
     * @return 书籍评价信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(evaluateInfoService.removeByIds(ids));
    }
}
