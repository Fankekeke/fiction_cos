package cc.mrbird.febs.cos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关系数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelateDTO {
    private Integer useId;
    private Integer itemId;
    private Double index;
}
