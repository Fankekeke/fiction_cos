package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户关注
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FollowInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 关注作者
     */
    private Integer authorId;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


}
