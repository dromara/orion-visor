package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.HostIdentityDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 主机身份 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Mapper
public interface HostIdentityDAO extends IMapper<HostIdentityDO> {

    /**
     * 设置 keyId 为 null
     *
     * @param keyId keyId
     * @return effect
     */
    int setKeyWithNull(@Param("keyId") Long keyId);

}
