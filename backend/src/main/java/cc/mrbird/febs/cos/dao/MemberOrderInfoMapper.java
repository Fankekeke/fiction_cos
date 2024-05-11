package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.MemberOrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

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

    /**
     * 本月收益
     *
     * @return 结果
     */
    BigDecimal selectIncomeMonth();

    /**
     * 本月工单
     *
     * @return 结果
     */
    Integer selectWorkOrderMonth();

    /**
     * 本年收益
     *
     * @return 结果
     */
    BigDecimal selectIncomeYear();

    /**
     * 本年工单
     *
     * @return 结果
     */
    Integer selectWorkOrderYear();

    /**
     * 获取缴费总收益
     *
     * @return 结果
     */
    BigDecimal selectAmountPrice();

    /**
     * 查询十天内缴费统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectPaymentRecord();

    /**
     * 查询十天内工单数量
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderRecord();
}
