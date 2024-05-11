package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.*;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.IMemberOrderInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberOrderInfoServiceImpl extends ServiceImpl<MemberOrderInfoMapper, MemberOrderInfo> implements IMemberOrderInfoService {

    private final UserInfoMapper userInfoMapper;

    private final BookInfoMapper bookInfoMapper;

    private final AuthorInfoMapper authorInfoMapper;

    private final BulletinInfoMapper bulletinInfoMapper;

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

    /**
     * 首页数据统计
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeData() {
        // 管理员展示信息
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        // 本月收益
        BigDecimal incomeMonth = baseMapper.selectIncomeMonth();
        // 本月工单
        Integer workOrderMonth = baseMapper.selectWorkOrderMonth();
        // 本年收益
        BigDecimal incomeYear = baseMapper.selectIncomeYear();
        // 本年工单
        Integer workOrderYear = baseMapper.selectWorkOrderYear();

        // 作家数量
        Integer authorNum = authorInfoMapper.selectCount(Wrappers.<AuthorInfo>lambdaQuery());
        // 用户数量
        Integer userNum = userInfoMapper.selectCount(Wrappers.<UserInfo>lambdaQuery());
        // 文章数量
        Integer bookNum = bookInfoMapper.selectCount(Wrappers.<BookInfo>lambdaQuery());
        // 总收益
        BigDecimal amount = baseMapper.selectAmountPrice();
        // 十天内缴费记录
        List<LinkedHashMap<String, Object>> paymentRecord = baseMapper.selectPaymentRecord();
        // 十天内工单数量
        List<LinkedHashMap<String, Object>> orderRecord = baseMapper.selectOrderRecord();
        result.put("incomeMonth", incomeMonth);
        result.put("workOrderMonth", workOrderMonth);
        result.put("incomeYear", incomeYear);
        result.put("workOrderYear", workOrderYear);

        result.put("userNum", userNum);
        result.put("authorNum", authorNum);
        result.put("bookNum", bookNum);
        result.put("totalRevenue", amount);

        result.put("paymentRecord", paymentRecord);
        result.put("orderRecord", orderRecord);

        // 公告信息
        List<BulletinInfo> bulletinList = bulletinInfoMapper.selectList(Wrappers.<BulletinInfo>lambdaQuery());
        result.put("bulletin", bulletinList);
        return result;
    }
}
