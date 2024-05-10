package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.FollowInfo;
import cc.mrbird.febs.cos.dao.FollowInfoMapper;
import cc.mrbird.febs.cos.service.IFollowInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class FollowInfoServiceImpl extends ServiceImpl<FollowInfoMapper, FollowInfo> implements IFollowInfoService {

    /**
     * 分页获取用户关注信息
     *
     * @param page       分页对象
     * @param followInfo 用户关注信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectFollowPage(Page<FollowInfo> page, FollowInfo followInfo) {
        return baseMapper.selectFollowPage(page, followInfo);
    }
}
