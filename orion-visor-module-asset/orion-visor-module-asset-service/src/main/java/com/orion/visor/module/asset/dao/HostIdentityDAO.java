package com.orion.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.HostIdentityDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
     * @param keyIdList keyIdList
     * @return effect
     */
    default int setKeyWithNull(List<Long> keyIdList) {
        LambdaUpdateWrapper<HostIdentityDO> updateWrapper = Wrappers.<HostIdentityDO>lambdaUpdate()
                .set(HostIdentityDO::getKeyId, null)
                .in(HostIdentityDO::getKeyId, keyIdList);
        return this.update(null, updateWrapper);
    }

}
