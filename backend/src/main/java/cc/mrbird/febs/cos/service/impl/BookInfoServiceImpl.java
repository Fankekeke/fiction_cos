package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.BookInfo;
import cc.mrbird.febs.cos.mapper.BookInfoMapper;
import cc.mrbird.febs.cos.service.IBookInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo> implements IBookInfoService {

}
