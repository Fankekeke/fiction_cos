package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.BookDetailInfo;
import cc.mrbird.febs.cos.dao.BookDetailInfoMapper;
import cc.mrbird.febs.cos.service.IBookDetailInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class BookDetailInfoServiceImpl extends ServiceImpl<BookDetailInfoMapper, BookDetailInfo> implements IBookDetailInfoService {

    /**
     * 分页获取书籍章节信息
     *
     * @param page           分页对象
     * @param bookDetailInfo 书籍章节信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDetailPage(Page<BookDetailInfo> page, BookDetailInfo bookDetailInfo) {
        return baseMapper.selectDetailPage(page, bookDetailInfo);
    }
}
