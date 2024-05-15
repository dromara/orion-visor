package com.orion.visor.module.infra.dao;

import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.framework.mybatis.core.query.Conditions;
import com.orion.visor.module.infra.entity.domain.PreferenceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户偏好 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
@Mapper
public interface PreferenceDAO extends IMapper<PreferenceDO> {

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    default int deleteByUserId(Long userId) {
        return this.delete(Conditions.eq(PreferenceDO::getUserId, userId));
    }

}
