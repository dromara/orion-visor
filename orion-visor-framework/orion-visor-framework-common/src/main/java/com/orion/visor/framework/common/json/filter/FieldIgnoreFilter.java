package com.orion.visor.framework.common.json.filter;

import com.alibaba.fastjson.serializer.PropertyFilter;
import com.orion.lang.utils.collect.Lists;

import java.util.List;

/**
 * 字段忽略过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/12 11:21
 */
public class FieldIgnoreFilter implements PropertyFilter {

    private final List<String> ignoreFields;

    public FieldIgnoreFilter(List<String> ignoreFields) {
        this.ignoreFields = ignoreFields;
    }

    @Override
    public boolean apply(Object object, String name, Object value) {
        return Lists.isEmpty(ignoreFields) || !ignoreFields.contains(name);
    }

}
