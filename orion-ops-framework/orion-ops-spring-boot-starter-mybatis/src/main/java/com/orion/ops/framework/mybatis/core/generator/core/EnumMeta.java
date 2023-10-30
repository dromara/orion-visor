package com.orion.ops.framework.mybatis.core.generator.core;

import com.orion.lang.define.collect.MultiLinkedHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * vue 枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 16:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnumMeta {

    /**
     * 类名称
     */
    private String className;

    /**
     * 备注
     */
    private String comment;

    /**
     * 配置
     */
    private MultiLinkedHashMap<String, String, Object> info;

}
