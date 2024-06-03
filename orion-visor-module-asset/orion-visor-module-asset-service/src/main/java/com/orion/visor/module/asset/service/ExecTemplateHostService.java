package com.orion.visor.module.asset.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 执行模板主机 服务类
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-22 12:13
 */
public interface ExecTemplateHostService {

    /**
     * 查询模板主机
     *
     * @param templateId templateId
     * @return hostId
     */
    Set<Long> getHostByTemplateId(Long templateId);

    /**
     * 查询模板主机
     *
     * @param templateIdList templateIdList
     * @return hostIdMap
     */
    Map<Long, Set<Long>> getHostByTemplateIdList(List<Long> templateIdList);

    /**
     * 设置模板主机
     *
     * @param templateId templateId
     * @param hostList   hostList
     */
    void setTemplateHost(Long templateId, List<Long> hostList);

    /**
     * 通过 templateId 删除
     *
     * @param templateId templateId
     * @return effect
     */
    Integer deleteByTemplateId(Long templateId);

    /**
     * 通过 templateId 删除
     *
     * @param templateIdList templateIdList
     * @return effect
     */
    Integer deleteByTemplateIdList(List<Long> templateIdList);

    /**
     * 通过 hostId 删除
     *
     * @param hostId hostId
     * @return effect
     */
    Integer deleteByHostId(Long hostId);

    /**
     * 通过 hostId 删除
     *
     * @param hostIdList hostIdList
     * @return effect
     */
    Integer deleteByHostIdList(List<Long> hostIdList);

}
