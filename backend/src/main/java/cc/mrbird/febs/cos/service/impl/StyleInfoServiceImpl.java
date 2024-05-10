package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.StyleInfo;
import cc.mrbird.febs.cos.dao.StyleInfoMapper;
import cc.mrbird.febs.cos.service.IStyleInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class StyleInfoServiceImpl extends ServiceImpl<StyleInfoMapper, StyleInfo> implements IStyleInfoService {

    /**
     * 分页获取阅读样式信息
     *
     * @param page      分页对象
     * @param styleInfo 阅读样式信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectStylePage(Page<StyleInfo> page, StyleInfo styleInfo) {
        return baseMapper.selectStylePage(page, styleInfo);
    }
}
