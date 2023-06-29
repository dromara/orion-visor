package com.orion.ops.framework.mybatis.type;

import org.apache.ibatis.type.TypeHandler;

import java.util.List;

/**
 * mybatis 类型转换
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 10:38
 */
public interface ITypeHandler<P, R> extends TypeHandler<R> {

    // FIXME KIT
    String COMMA = ",";

    /**
     * 数据类型转换
     *
     * @param param param
     * @return result
     */
    R getResult(P param);

    /**
     * // FIXME kit
     * 用 , 连接
     *
     * @param list list
     * @return res
     */
    default String join(List<?> list) {
        if (list == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = list.size(); i < size; i++) {
            sb.append(list.get(i));
            if (i != size - 1) {
                sb.append(COMMA);
            }
        }
        return sb.toString();
    }

}
