package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 书籍章节管理
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BookDetailInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属书籍
     */
    private Integer bookId;

    /**
     * 章节名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer indexNo;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 字数
     */
    private Integer words;

    /**
     * 是否需要会员（0.否 1.是）
     */
    private String checkFlag;

    /**
     * 发布时间
     */
    private LocalDateTime createDate;

    /**
     * 文章内容
     */
    private String content;


}
