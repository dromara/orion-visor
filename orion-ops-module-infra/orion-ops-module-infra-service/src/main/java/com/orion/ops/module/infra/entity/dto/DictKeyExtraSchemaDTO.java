package com.orion.ops.module.infra.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典配置项额外参数类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/20 18:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictKeyExtraSchemaDTO {

    /**
     * 参数
     */
    private String name;

    /**
     * 数据类型
     */
    private String type;

}
