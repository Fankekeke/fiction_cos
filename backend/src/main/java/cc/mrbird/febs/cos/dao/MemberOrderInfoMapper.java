package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.MemberOrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface MemberOrderInfoMapper extends BaseMapper<MemberOrderInfo> {

    /**
     * 分页获取会员订单信息
     *
     * @param page            分页对象
     * @param memberOrderInfo 会员订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderByPage(Page<MemberOrderInfo> page, @Param("memberOrderInfo") MemberOrderInfo memberOrderInfo);
}
