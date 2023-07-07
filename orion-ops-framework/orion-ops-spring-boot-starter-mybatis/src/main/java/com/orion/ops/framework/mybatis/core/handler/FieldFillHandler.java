package com.orion.ops.framework.mybatis.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.orion.ops.framework.common.security.SecurityHolder;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import org.apache.ibatis.reflection.MetaObject;

import javax.annotation.Resource;
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

    @Resource
    private SecurityHolder securityHolder;

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

            // TODO TEST
            Long userId = securityHolder.getLoginUserId();
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
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            // 更新时间
            Object updateTime = getFieldValByName("updateTime", metaObject);
            if (Objects.isNull(updateTime)) {
                setFieldValByName("updateTime", new Date(), metaObject);
            }

            // 更新人
            Object updater = getFieldValByName("updater", metaObject);
            // TODO TEST
            Long userId = securityHolder.getLoginUserId();
            if (Objects.nonNull(userId) && Objects.isNull(updater)) {
                setFieldValByName("updater", userId.toString(), metaObject);
            }
        }
    }

}
