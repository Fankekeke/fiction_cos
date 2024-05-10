package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.BookInfo;
import cc.mrbird.febs.cos.dao.BookInfoMapper;
import cc.mrbird.febs.cos.service.IBookInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo> implements IBookInfoService {

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
}
