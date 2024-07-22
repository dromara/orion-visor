package com.orion.visor.module.asset.service;

import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import com.orion.visor.module.asset.entity.domain.HostDO;
import com.orion.visor.module.asset.enums.HostTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * 主机配置 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
public interface HostConfigService {

    /**
     * 获取主机配置
     *
     * @param id   id
     * @param type type
     * @param <T>  T
     * @return host
     */
    <T extends GenericsDataModel> T getHostConfig(Long id, HostTypeEnum type);

    /**
     * 构建主机配置
     *
     * @param host host
     * @param type type
     * @param <T>  T
     * @return host
     */
    <T extends GenericsDataModel> T buildHostConfig(HostDO host, HostTypeEnum type);

    /**
     * 获取主机配置
     *
     * @param idList idList
     * @param type   type
     * @param <T>    T
     * @return config
     */
    <T extends GenericsDataModel> Map<Long, T> getHostConfigMap(List<Long> idList, HostTypeEnum type);

    /**
     * 构建主机配置
     *
     * @param hostList hostList
     * @param type     type
     * @param <T>      T
     * @return config
     */
    <T extends GenericsDataModel> Map<Long, T> buildHostConfigMap(List<HostDO> hostList, HostTypeEnum type);

}
