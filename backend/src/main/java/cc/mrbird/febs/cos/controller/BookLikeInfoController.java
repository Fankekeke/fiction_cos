package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.BookDetailInfo;
import cc.mrbird.febs.cos.entity.BookLikeInfo;
import cc.mrbird.febs.cos.entity.FollowInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IBookLikeInfoService;
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
@RequestMapping("/cos/book-like-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookLikeInfoController {

    private final IBookLikeInfoService bookLikeInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取书籍点赞信息
     *
     * @param page         分页对象
     * @param bookLikeInfo 书籍点赞信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<BookLikeInfo> page, BookLikeInfo bookLikeInfo) {
        return R.ok(bookLikeInfoService.selectBookLikePage(page, bookLikeInfo));
    }

    /**
     * 书籍点赞信息详情
     *
     * @param id 书籍点赞ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bookLikeInfoService.getById(id));
    }

    /**
     * 书籍点赞信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bookLikeInfoService.list());
    }

    /**
     * 新增书籍点赞信息
     *
     * @param bookLikeInfo 书籍点赞信息
     * @return 结果
     */
    @PostMapping
    public R save(BookLikeInfo bookLikeInfo) {
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, bookLikeInfo.getUserId()));
        if (userInfo != null) {
            bookLikeInfo.setUserId(userInfo.getId());
        }
        // 判断用户是否关注
        int count = bookLikeInfoService.count(Wrappers.<BookLikeInfo>lambdaQuery().eq(BookLikeInfo::getBookId, bookLikeInfo.getAuthorId()).eq(BookLikeInfo::getUserId, userInfo.getId()));
        if (count > 0) {
            return R.ok(true);
        } else {
            bookLikeInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            return R.ok(bookLikeInfoService.save(bookLikeInfo));
        }
    }

    /**
     * 修改书籍点赞信息
     *
     * @param bookLikeInfo 书籍点赞信息
     * @return 结果
     */
    @PutMapping
    public R edit(BookLikeInfo bookLikeInfo) {
        return R.ok(bookLikeInfoService.updateById(bookLikeInfo));
    }

    /**
     * 删除书籍点赞信息
     *
     * @param ids ids
     * @return 书籍点赞信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bookLikeInfoService.removeByIds(ids));
    }
}
