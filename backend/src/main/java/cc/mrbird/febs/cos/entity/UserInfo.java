package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户管理
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String code;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 性别（0.男 1.女）
     */
    private String sex;

    /**
     * 头像
     */
    private String images;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 备注
     */
    private String contet;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 所属用户
     */
    private Integer userId;


}
