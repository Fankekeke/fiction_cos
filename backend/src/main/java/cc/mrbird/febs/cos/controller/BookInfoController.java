package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AuthorInfo;
import cc.mrbird.febs.cos.entity.BookInfo;
import cc.mrbird.febs.cos.service.IAuthorInfoService;
import cc.mrbird.febs.cos.service.IBookInfoService;
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
@RequestMapping("/cos/book-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookInfoController {

    private final IBookInfoService bookInfoService;

    private final IAuthorInfoService authorInfoService;

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
     * 图书状态审核
     *
     * @param id     主键ID
     * @param status 状态
     * @return 结果
     */
    @GetMapping("/audit")
    public R audit(Integer id, String status) {
        return R.ok(bookInfoService.update(Wrappers.<BookInfo>lambdaUpdate().set(BookInfo::getStatus, status).eq(BookInfo::getId, id)));
    }

    /**
     * 书籍信息详情
     *
     * @param id 书籍ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bookInfoService.selectDetailById(id));
    }

    /**
     * 根据用户ID获取书籍信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/list/byUserId")
    public R selectBookByUserId(Integer userId) {
        return R.ok(bookInfoService.selectBookByUserId(userId));
    }

    /**
     * 文章统计列表
     *
     * @return 结果
     */
    @GetMapping("/selectListDetail")
    public R selectListDetail() {
        return R.ok(bookInfoService.selectListDetail());
    }

    /**
     * 文章流量卡排行列表
     *
     * @return 结果
     */
    @GetMapping("/selectListTop")
    public R selectListTop() {
        return R.ok(bookInfoService.selectListTop());
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
        // 获取作者信息
        AuthorInfo authorInfo = authorInfoService.getOne(Wrappers.<AuthorInfo>lambdaQuery().eq(AuthorInfo::getUserId, bookInfo.getAuthorId()));
        if (authorInfo != null) {
            bookInfo.setAuthorId(authorInfo.getId());
        }

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
        bookInfo.setStatus("0");
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
