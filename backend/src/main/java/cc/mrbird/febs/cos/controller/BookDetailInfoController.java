package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.BookDetailInfo;
import cc.mrbird.febs.cos.service.IBookDetailInfoService;
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
@RequestMapping("/cos/book-detail-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookDetailInfoController {

    private final IBookDetailInfoService bookDetailInfoService;

    /**
     * 分页获取书籍章节信息
     *
     * @param page           分页对象
     * @param bookDetailInfo 书籍章节信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<BookDetailInfo> page, BookDetailInfo bookDetailInfo) {
        return R.ok(bookDetailInfoService.selectDetailPage(page, bookDetailInfo));
    }

    /**
     * 书籍章节信息详情
     *
     * @param id 书籍章节ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bookDetailInfoService.getById(id));
    }

    /**
     * 书籍章节信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bookDetailInfoService.list());
    }

    /**
     * 新增书籍章节信息
     *
     * @param bookDetailInfo 书籍章节信息
     * @return 结果
     */
    @PostMapping
    public R save(BookDetailInfo bookDetailInfo) {
        bookDetailInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(bookDetailInfoService.save(bookDetailInfo));
    }

    /**
     * 修改书籍章节信息
     *
     * @param bookDetailInfo 书籍章节信息
     * @return 结果
     */
    @PutMapping
    public R edit(BookDetailInfo bookDetailInfo) {
        return R.ok(bookDetailInfoService.updateById(bookDetailInfo));
    }

    /**
     * 删除书籍章节信息
     *
     * @param ids ids
     * @return 书籍章节信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bookDetailInfoService.removeByIds(ids));
    }
}
