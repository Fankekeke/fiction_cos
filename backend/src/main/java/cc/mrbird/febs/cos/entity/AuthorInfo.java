package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 作家管理
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuthorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创作者编号
     */
    private String code;

    /**
     * 创作者名称
     */
    private String name;

    /**
     * 备注
     */
    private String content;

    /**
     * 头像
     */
    private String images;

    /**
     * 性别（0.男 1.女）
     */
    private String sex;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 所属用户
     */
    private Integer userId;


}
