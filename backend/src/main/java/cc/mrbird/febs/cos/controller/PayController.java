package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AlipayBean;
import cc.mrbird.febs.cos.entity.MemberOrderInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IMemberOrderInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cc.mrbird.febs.cos.service.PayService;
import cn.hutool.core.date.DateUtil;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/cos/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IMemberOrderInfoService memberOrderInfoService;

    /**
     * 阿里支付
     * @param subject
     * @param body
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/alipay")
    public R alipay(String outTradeNo, String subject, String totalAmount, String body) throws AlipayApiException {
        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(outTradeNo);
        alipayBean.setSubject(subject);
        alipayBean.setTotal_amount(totalAmount);
        alipayBean.setBody(body);
        String result = payService.aliPay(alipayBean);
        return R.ok(result);
    }

    /**
     * 购买会员
     * @param subject
     * @param body
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/member")
    public R alipayMember(String subject, String totalAmount, String body, Integer ruleId, Integer userId) throws AlipayApiException {
        // 获取用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));

        MemberOrderInfo orderInfo = new MemberOrderInfo();
        orderInfo.setStatus("0");
        orderInfo.setCode("OR-" + System.currentTimeMillis());
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setUserId(userInfo.getId());
        orderInfo.setPrice(new BigDecimal(totalAmount));
        orderInfo.setType(ruleId);

        memberOrderInfoService.save(orderInfo);

        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(orderInfo.getCode());
        alipayBean.setSubject(subject);
        alipayBean.setTotal_amount(totalAmount);
        alipayBean.setBody(body);
        String result = payService.aliPay(alipayBean);
        return R.ok(result);
    }

}
