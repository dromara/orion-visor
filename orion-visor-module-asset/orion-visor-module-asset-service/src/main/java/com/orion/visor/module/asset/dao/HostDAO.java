package com.orion.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.HostDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Arrays;
import java.util.List;

/**
 * 主机 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Mapper
public interface HostDAO extends IMapper<HostDO> {

    List<SFunction<HostDO, ?>> BASE_COLUMN = Arrays.asList(
            HostDO::getId,
            HostDO::getType,
            HostDO::getName,
            HostDO::getCode,
            HostDO::getAddress,
            HostDO::getPort,
            HostDO::getStatus,
            HostDO::getCreateTime,
            HostDO::getUpdateTime,
            HostDO::getCreator,
            HostDO::getUpdater
    );

    /**
     * 通过 id 查询基本信息
     *
     * @param id id
     * @return id
     */
    default HostDO selectBaseById(Long id) {
        return this.of()
                .createWrapper()
                .select(BASE_COLUMN)
                .eq(HostDO::getId, id)
                .then()
                .getOne();
    }

    /**
     * 通过 id 查询基本信息
     *
     * @param idList idList
     * @return id
     */
    default List<HostDO> selectBaseByIdList(List<Long> idList) {
        return this.of()
                .createWrapper()
                .select(BASE_COLUMN)
                .in(HostDO::getId, idList)
                .then()
                .list();
    }

    /**
     * 获取的 hostId
     *
     * @param hostIdList hostIdList
     * @param type       type
     * @param status     status
     * @return hostId
     */
    default List<Long> getHostIdList(List<Long> hostIdList, String type, String status) {
        return this.of()
                .createWrapper(true)
                .select(HostDO::getId)
                .in(HostDO::getId, hostIdList)
                .eq(HostDO::getType, type)
                .eq(HostDO::getStatus, status)
                .then()
                .list(HostDO::getId);
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
