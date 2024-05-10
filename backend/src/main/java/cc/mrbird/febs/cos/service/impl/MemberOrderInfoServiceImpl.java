package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.MemberOrderInfo;
import cc.mrbird.febs.cos.dao.MemberOrderInfoMapper;
import cc.mrbird.febs.cos.service.IMemberOrderInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class MemberOrderInfoServiceImpl extends ServiceImpl<MemberOrderInfoMapper, MemberOrderInfo> implements IMemberOrderInfoService {

    /**
     * 分页获取会员订单信息
     *
     * @param page            分页对象
     * @param memberOrderInfo 会员订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderByPage(Page<MemberOrderInfo> page, MemberOrderInfo memberOrderInfo) {
        return baseMapper.selectOrderByPage(page, memberOrderInfo);
    }
}
