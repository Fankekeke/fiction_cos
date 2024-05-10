package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户会员购买记录
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MemberOrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 会员开始时间
     */
    private String startDate;

    /**
     * 会员结束时间
     */
    private String endDate;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 支付时间
     */
    private String createDate;

    /**
     * 状态（0.未支付 1.已支付）
     */
    private String status;

    @TableField(exist = false)
    private String userName;


}
