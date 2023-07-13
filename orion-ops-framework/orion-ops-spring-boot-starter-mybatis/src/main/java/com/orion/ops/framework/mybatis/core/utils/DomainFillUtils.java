package com.orion.ops.framework.mybatis.core.utils;

import com.orion.ops.framework.common.security.SecurityHolder;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;

import java.util.Date;
import java.util.Objects;

/**
 * 对象填充器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 10:54
 */
public class DomainFillUtils {

    /**
     * 安全信息持有者
     */
    private static SecurityHolder securityHolder;

    /**
     * 填充插入
     *
     * @param baseDO baseDO
     */
    public static void fillInsert(BaseDO baseDO) {
        Date now = new Date();
        // 创建时间
        if (Objects.isNull(baseDO.getCreateTime())) {
            baseDO.setCreateTime(now);
        }
        // 更新时间
        if (Objects.isNull(baseDO.getUpdateTime())) {
            baseDO.setUpdateTime(now);
        }

        Long userId = securityHolder.getLoginUserId();
        // 创建人
        if (Objects.nonNull(userId) && Objects.isNull(baseDO.getCreator())) {
            baseDO.setCreator(userId.toString());
        }
        // 更新人
        if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdater())) {
            baseDO.setUpdater(userId.toString());
        }
        // 逻辑删除字段
        if (Objects.isNull(baseDO.getDeleted())) {
            baseDO.setDeleted(false);
        }
    }

    /**
     * 填充更新
     *
     * @param baseDO baseDO
     */
    public static void fillUpdate(BaseDO baseDO) {
        // 更新时间
        if (Objects.isNull(baseDO.getUpdateTime())) {
            baseDO.setUpdateTime(new Date());
        }
        // 更新人
        Long userId = securityHolder.getLoginUserId();
        if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdater())) {
            baseDO.setUpdater(userId.toString());
        }
    }

    public static void setSecurityHolder(SecurityHolder securityHolder) {
        DomainFillUtils.securityHolder = securityHolder;
    }

}
