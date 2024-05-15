package com.orion.visor.framework.mybatis.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import com.orion.visor.framework.mybatis.core.utils.DomainFillUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Objects;

/**
 * 字段填充处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 19:01
 */
public class FieldFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            // 填充插入
            DomainFillUtils.fillInsert((BaseDO) metaObject.getOriginalObject());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            // 填充更新
            DomainFillUtils.fillUpdate((BaseDO) metaObject.getOriginalObject());
        }
    }

}
