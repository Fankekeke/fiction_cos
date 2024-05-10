package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.AuthorInfo;
import cc.mrbird.febs.cos.dao.AuthorInfoMapper;
import cc.mrbird.febs.cos.service.IAuthorInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class AuthorInfoServiceImpl extends ServiceImpl<AuthorInfoMapper, AuthorInfo> implements IAuthorInfoService {

    /**
     * 分页获取作家信息
     *
     * @param page       分页对象
     * @param authorInfo 作家信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectAuthorPage(Page<AuthorInfo> page, AuthorInfo authorInfo) {
        return baseMapper.selectAuthorPage(page, authorInfo);
    }
}
