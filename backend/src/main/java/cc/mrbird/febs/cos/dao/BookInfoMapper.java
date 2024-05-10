package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.BookInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface BookInfoMapper extends BaseMapper<BookInfo> {

    /**
     * 分页获取书籍信息
     *
     * @param page     分页对象
     * @param bookInfo 书籍信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectBookPage(Page<BookInfo> page, @Param("bookInfo") BookInfo bookInfo);
}
