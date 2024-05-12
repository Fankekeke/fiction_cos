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
 * 书籍管理
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BookInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 书籍ID
     */
    private String code;

    /**
     * 书籍名称
     */
    private String name;

    /**
     * 作者ID
     */
    private Integer authorId;

    /**
     * 最后更新时间
     */
    private String updateDate;

    /**
     * 最后章节
     */
    private String lastChapter;

    /**
     * 标签
     */
    private String tag;

    /**
     * 作品介绍
     */
    private String content;

    /**
     * 书籍图片
     */
    private String images;

    /**
     * 作品分类
     */
    private String type;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 审核状态（0.审核中 1.通过 2.驳回）
     */
    private String status;

    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private Integer views;

    @TableField(exist = false)
    private AuthorInfo authorInfo;

    @TableField(exist = false)
    private Integer userId;

}
