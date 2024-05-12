package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.BookDetailInfo;
import cc.mrbird.febs.cos.entity.BookInfo;
import cc.mrbird.febs.cos.service.IBookDetailInfoService;
import cc.mrbird.febs.cos.service.IBookInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/book-detail-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookDetailInfoController {

    private final IBookDetailInfoService bookDetailInfoService;

    private final IBookInfoService bookInfoService;

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
     * 更新浏览量
     *
     * @param detailId 章节ID
     * @return 结果
     */
    @GetMapping("/views/edit")
    public R updateViews(Integer detailId) {
        BookDetailInfo bookDetailInfo = bookDetailInfoService.getById(detailId);
        bookDetailInfo.setViews(bookDetailInfo.getViews() + 1);
        bookDetailInfoService.updateById(bookDetailInfo);
        return R.ok(true);
    }

    /**
     * 根据图书ID获取章节信息
     *
     * @param bookId 图书ID
     * @return 结果
     */
    @GetMapping("/list/book")
    public R selectDetailByBookId(Integer bookId) {
        List<BookDetailInfo> detailList = bookDetailInfoService.list(Wrappers.<BookDetailInfo>lambdaQuery().eq(BookDetailInfo::getBookId, bookId).orderByDesc(BookDetailInfo::getIndexNo));
        if (CollectionUtil.isEmpty(detailList)) {
            return R.ok(Collections.emptyList());
        } else {
            return R.ok(detailList);
        }
    }

    /**
     * 根据图书ID获取章节统计
     *
     * @param bookId 图书ID
     * @return 结果
     */
    @GetMapping("/rate")
    public R selectBookDetailRate(Integer bookId) {
        List<BookDetailInfo> detailList = bookDetailInfoService.list(Wrappers.<BookDetailInfo>lambdaQuery().eq(BookDetailInfo::getBookId, bookId).orderByDesc(BookDetailInfo::getIndexNo));
        if (CollectionUtil.isEmpty(detailList)) {
            return R.ok(Collections.emptyList());
        }

        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>(detailList.size());
        for (BookDetailInfo bookDetailInfo : detailList) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("name", bookDetailInfo.getName());
                    put("views", (bookDetailInfo.getViews() == null ? 0 : bookDetailInfo.getViews()));
                }
            };
            result.add(item);
        }
        return R.ok(result);
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
        // 更新书籍信息
        BookInfo bookInfo = bookInfoService.getById(bookDetailInfo.getBookId());
        bookInfo.setLastChapter(bookDetailInfo.getName());
        bookInfo.setUpdateDate(DateUtil.formatDateTime(new Date()));
        bookInfoService.updateById(bookInfo);

        bookDetailInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 字数
        if (StrUtil.isNotEmpty(bookDetailInfo.getContent())) {
            bookDetailInfo.setWords(StrUtil.length(bookDetailInfo.getContent()));
        }
        // 浏览量
        bookDetailInfo.setViews(0);
        // 排序
        List<BookDetailInfo> detailInfoList = bookDetailInfoService.list(Wrappers.<BookDetailInfo>lambdaQuery().eq(BookDetailInfo::getBookId, bookDetailInfo.getBookId()).orderByDesc(BookDetailInfo::getIndexNo));
        if (CollectionUtil.isEmpty(detailInfoList)) {
            bookDetailInfo.setIndexNo(1);
        } else {
            bookDetailInfo.setIndexNo(detailInfoList.get(0).getIndexNo() + 1);
        }

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
        // 更新书籍信息
        BookInfo bookInfo = bookInfoService.getById(bookDetailInfo.getBookId());
        bookInfo.setUpdateDate(DateUtil.formatDateTime(new Date()));


        // 排序
        List<BookDetailInfo> detailInfoList = bookDetailInfoService.list(Wrappers.<BookDetailInfo>lambdaQuery().eq(BookDetailInfo::getBookId, bookDetailInfo.getBookId()).orderByDesc(BookDetailInfo::getIndexNo));
        if (CollectionUtil.isNotEmpty(detailInfoList) && detailInfoList.get(0).getId().equals(bookDetailInfo.getId())) {
            bookInfo.setLastChapter(bookDetailInfo.getName());
        }

        // 字数
        if (StrUtil.isNotEmpty(bookDetailInfo.getContent())) {
            bookDetailInfo.setWords(StrUtil.length(bookDetailInfo.getContent()));
        }
        bookInfoService.updateById(bookInfo);

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
