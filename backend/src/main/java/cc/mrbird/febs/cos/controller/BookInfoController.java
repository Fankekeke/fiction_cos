package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.BookInfo;
import cc.mrbird.febs.cos.service.IBookInfoService;
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
@RequestMapping("/cos/book-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookInfoController {

    private final IBookInfoService bookInfoService;

    /**
     * 分页获取书籍信息
     *
     * @param page     分页对象
     * @param bookInfo 书籍信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<BookInfo> page, BookInfo bookInfo) {
        return R.ok(bookInfoService.selectBookPage(page, bookInfo));
    }

    /**
     * 书籍信息详情
     *
     * @param id 书籍ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bookInfoService.getById(id));
    }

    /**
     * 书籍信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bookInfoService.list());
    }

    /**
     * 新增书籍信息
     *
     * @param bookInfo 书籍信息
     * @return 结果
     */
    @PostMapping
    public R save(BookInfo bookInfo) {
        bookInfo.setCode("BK-" + System.currentTimeMillis());
        bookInfo.setStatus("0");
        bookInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(bookInfoService.save(bookInfo));
    }

    /**
     * 修改书籍信息
     *
     * @param bookInfo 书籍信息
     * @return 结果
     */
    @PutMapping
    public R edit(BookInfo bookInfo) {
        return R.ok(bookInfoService.updateById(bookInfo));
    }

    /**
     * 删除书籍信息
     *
     * @param ids ids
     * @return 书籍信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bookInfoService.removeByIds(ids));
    }
}
