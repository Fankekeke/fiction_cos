package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.StyleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface StyleInfoMapper extends BaseMapper<StyleInfo> {

    /**
     * 分页获取阅读样式信息
     *
     * @param page      分页对象
     * @param styleInfo 阅读样式信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStylePage(Page<StyleInfo> page, @Param("styleInfo") StyleInfo styleInfo);
}
