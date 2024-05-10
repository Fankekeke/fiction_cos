package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.BookLikeInfo;
import cc.mrbird.febs.cos.dao.BookLikeInfoMapper;
import cc.mrbird.febs.cos.service.IBookLikeInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class BookLikeInfoServiceImpl extends ServiceImpl<BookLikeInfoMapper, BookLikeInfo> implements IBookLikeInfoService {

    /**
     * 分页获取书籍点赞信息
     *
     * @param page         分页对象
     * @param bookLikeInfo 书籍点赞信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectBookLikePage(Page<BookLikeInfo> page, BookLikeInfo bookLikeInfo) {
        return baseMapper.selectBookLikePage(page, bookLikeInfo);
    }
}
