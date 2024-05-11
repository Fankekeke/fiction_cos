package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.AuthorInfoMapper;
import cc.mrbird.febs.cos.dao.BookDetailInfoMapper;
import cc.mrbird.febs.cos.entity.AuthorInfo;
import cc.mrbird.febs.cos.entity.BookDetailInfo;
import cc.mrbird.febs.cos.entity.BookInfo;
import cc.mrbird.febs.cos.dao.BookInfoMapper;
import cc.mrbird.febs.cos.service.IBookInfoService;
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

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo> implements IBookInfoService {

    private final AuthorInfoMapper authorInfoMapper;

    private final BookDetailInfoMapper bookDetailInfoMapper;

    /**
     * 分页获取书籍信息
     *
     * @param page     分页对象
     * @param bookInfo 书籍信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectBookPage(Page<BookInfo> page, BookInfo bookInfo) {
        return baseMapper.selectBookPage(page, bookInfo);
    }

    /**
     * 书籍信息详情
     *
     * @param bookId 书籍ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectDetailById(Integer bookId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("book", null);
                put("detail", Collections.emptyList());
                put("author", null);
            }
        };

        // 图书信息
        BookInfo bookInfo = this.getById(bookId);
        result.put("book", bookInfo);
        // 图书章节
        List<BookDetailInfo> detailList = bookDetailInfoMapper.selectList(Wrappers.<BookDetailInfo>lambdaQuery().eq(BookDetailInfo::getBookId, bookId).orderByDesc(BookDetailInfo::getIndexNo));
        result.put("detail", detailList);
        // 作者信息
        AuthorInfo authorInfo = authorInfoMapper.selectById(bookInfo.getAuthorId());
        result.put("author", authorInfo);

        return result;
    }

    /**
     * 根据用户ID获取书籍信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public List<BookInfo> selectBookByUserId(Integer userId) {
        // 获取作者信息
        AuthorInfo authorInfo = authorInfoMapper.selectOne(Wrappers.<AuthorInfo>lambdaQuery().eq(AuthorInfo::getUserId, userId));

        if (authorInfo == null) {
            return Collections.emptyList();
        }

        return this.list(Wrappers.<BookInfo>lambdaQuery().eq(BookInfo::getAuthorId, authorInfo.getId()));
    }
}
