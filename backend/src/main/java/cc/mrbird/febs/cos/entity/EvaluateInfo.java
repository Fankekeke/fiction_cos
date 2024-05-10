package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 书籍评价
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EvaluateInfo implements Serializable {

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
     * 书籍ID
     */
    private Integer bookId;

    /**
     * 内容
     */
    private String content;

    /**
     * 分数
     */
    private String score;

    /**
     * 创建时间
     */
    private String createDate;

    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String bookName;
    @TableField(exist = false)
    private String authorName;

}
