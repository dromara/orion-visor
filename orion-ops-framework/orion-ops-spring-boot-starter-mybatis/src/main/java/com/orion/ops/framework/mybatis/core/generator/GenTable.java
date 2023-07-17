package com.orion.ops.framework.mybatis.core.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/17 10:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenTable {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 注释
     */
    private String comment;

    /**
     * 请求实体包名
     */
    private String requestPackage;

}
