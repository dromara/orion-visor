package com.orion.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.HostConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 主机配置 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Mapper
public interface HostConfigDAO extends IMapper<HostConfigDO> {

    /**
     * 通过 hostId 查询主机配置
     *
     * @param hostId hostId
     * @param type   type
     * @return row
     */
    default HostConfigDO getHostConfigByHostId(Long hostId, String type) {
        // 条件
        LambdaQueryWrapper<HostConfigDO> wrapper = this.lambda()
                .eq(HostConfigDO::getHostId, hostId)
                .eq(HostConfigDO::getType, type);
        // 查询
        return this.of(wrapper).getOne();
    }

    /**
     * 通过 hostId 查询主机配置
     *
     * @param hostId hostId
     * @return rows
     */
    default List<HostConfigDO> getHostConfigByHostId(Long hostId) {
        // 条件
        LambdaQueryWrapper<HostConfigDO> wrapper = this.lambda()
                .eq(HostConfigDO::getHostId, hostId);
        // 查询
        return this.of(wrapper).list();
    }

    /**
     * 通过 hostId 批量查询主机配置
     *
     * @param hostIdList hostIdList
     * @param type       type
     * @return rows
     */
    default List<HostConfigDO> getHostConfigByHostIdList(List<Long> hostIdList, String type) {
        // 条件
        LambdaQueryWrapper<HostConfigDO> wrapper = this.wrapper()
                .eq(HostConfigDO::getType, type)
                .in(HostConfigDO::getHostId, hostIdList);
        // 查询
        return this.of(wrapper).list();
    }

    /**
     * 通过 hostId 删除主机配置
     *
     * @param hostId hostId
     * @return effect
     */
    default Integer deleteByHostId(Long hostId) {
        // 条件
        LambdaQueryWrapper<HostConfigDO> wrapper = this.lambda()
                .eq(HostConfigDO::getHostId, hostId);
        // 删除
        return this.delete(wrapper);
    }

    /**
     * 通过 hostId 批量删除主机配置
     *
     * @param hostIdList hostIdList
     * @return effect
     */
    default Integer deleteByHostIdList(List<Long> hostIdList) {
        // 条件
        LambdaQueryWrapper<HostConfigDO> wrapper = this.lambda()
                .in(HostConfigDO::getHostId, hostIdList);
        // 删除
        return this.delete(wrapper);
    }

    /**
     * 设置 keyId 为 NULL
     *
     * @param keyIdList keyIdList
     * @return effect
     */
    int setKeyIdWithNull(@Param("keyIdList") List<Long> keyIdList);

    /**
     * 设置 identityId 为 NULL
     *
     * @param identityIdList identityIdList
     * @return effect
     */
    int setIdentityIdWithNull(@Param("identityIdList") List<Long> identityIdList);

}
