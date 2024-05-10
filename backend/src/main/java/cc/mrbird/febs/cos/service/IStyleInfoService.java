package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.StyleInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface IStyleInfoService extends IService<StyleInfo> {

    /**
     * 分页获取阅读样式信息
     *
     * @param page      分页对象
     * @param styleInfo 阅读样式信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStylePage(Page<StyleInfo> page, StyleInfo styleInfo);
}
