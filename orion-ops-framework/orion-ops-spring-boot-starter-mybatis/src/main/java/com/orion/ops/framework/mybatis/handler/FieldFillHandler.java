package com.orion.ops.framework.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.orion.ops.framework.mybatis.domain.BaseDO;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;
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
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();

            Date now = new Date();
            // 创建时间
            if (Objects.isNull(baseDO.getCreateTime())) {
                baseDO.setCreateTime(now);
            }
            // 更新时间
            if (Objects.isNull(baseDO.getUpdateTime())) {
                baseDO.setUpdateTime(now);
            }

            // FIXME 当前用户
            Long userId = null;
            // 创建人
            if (Objects.nonNull(userId) && Objects.isNull(baseDO.getCreator())) {
                baseDO.setCreator(userId.toString());
            }
            // 更新人
            if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdater())) {
                baseDO.setUpdater(userId.toString());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间
        Object modifyTime = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }

        // 更新人
        Object updater = getFieldValByName("updater", metaObject);
        // FIXME 当前用户
        Long userId = null;
        if (Objects.nonNull(userId) && Objects.isNull(updater)) {
            setFieldValByName("updater", userId.toString(), metaObject);
        }
    }

}
