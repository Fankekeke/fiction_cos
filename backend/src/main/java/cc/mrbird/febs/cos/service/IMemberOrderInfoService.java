package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.MemberOrderInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface IMemberOrderInfoService extends IService<MemberOrderInfo> {

    /**
     * 分页获取会员订单信息
     *
     * @param page            分页对象
     * @param memberOrderInfo 会员订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderByPage(Page<MemberOrderInfo> page, MemberOrderInfo memberOrderInfo);

    /**
     * 首页数据统计
     *
     * @return 结果
     */
    LinkedHashMap<String, Object> homeData();
}
