package com.orion.visor.module.infra.service;

import com.alibaba.fastjson.JSONObject;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.infra.entity.request.dict.DictValueCreateRequest;
import com.orion.visor.module.infra.entity.request.dict.DictValueQueryRequest;
import com.orion.visor.module.infra.entity.request.dict.DictValueRollbackRequest;
import com.orion.visor.module.infra.entity.request.dict.DictValueUpdateRequest;
import com.orion.visor.module.infra.entity.vo.DictValueVO;

import java.util.List;
import java.util.Map;

/**
 * 字典配置值 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
public interface DictValueService {

    /**
     * 创建字典配置值
     *
     * @param request request
     * @return id
     */
    Long createDictValue(DictValueCreateRequest request);

    /**
     * 更新字典配置值
     *
     * @param request request
     * @return effect
     */
    Integer updateDictValueById(DictValueUpdateRequest request);

    /**
     * 更新字典配置值
     *
     * @param request request
     * @return effect
     */
    Integer rollbackDictValueById(DictValueRollbackRequest request);

    /**
     * 查询字典配置值
     *
     * @param keys keys
     * @return rows
     */
    Map<String, List<JSONObject>> getDictValueList(List<String> keys);

    /**
     * 分页查询字典配置值
     *
     * @param request request
     * @return rows
     */
    DataGrid<DictValueVO> getDictValuePage(DictValueQueryRequest request);

    /**
     * 通过 keyId 更新 keyName
     *
     * @param keyId     keyId
     * @param beforeKey beforeKey
     * @param newKey    newKey
     * @return effect
     */
    Integer updateKeyNameByKeyId(Long keyId, String beforeKey, String newKey);

    /**
     * 通过 id 删除字典配置值
     *
     * @param id id
     * @return effect
     */
    Integer deleteDictValueById(Long id);

    /**
     * 通过 id 批量删除字典配置值
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteDictValueByIdList(List<Long> idList);

    /**
     * 通过 keyId 删除字典配置值
     *
     * @param keyId keyId
     * @return effect
     */
    Integer deleteDictValueByKeyId(Long keyId);

    /**
     * 通过 keyId 批量删除字典配置值
     *
     * @param keyIdList keyIdList
     * @return effect
     */
    Integer deleteDictValueByKeyIdList(List<Long> keyIdList);

}
