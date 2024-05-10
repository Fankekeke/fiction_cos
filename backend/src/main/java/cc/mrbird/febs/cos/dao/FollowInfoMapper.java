package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.FollowInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface FollowInfoMapper extends BaseMapper<FollowInfo> {

    /**
     * 分页获取用户关注信息
     *
     * @param page       分页对象
     * @param followInfo 用户关注信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectFollowPage(Page<FollowInfo> page, @Param("followInfo") FollowInfo followInfo);
}
