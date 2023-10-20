package com.orion.ops.module.infra.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.infra.entity.request.dict.DictKeyCreateRequest;
import com.orion.ops.module.infra.entity.request.dict.DictKeyQueryRequest;
import com.orion.ops.module.infra.entity.request.dict.DictKeyUpdateRequest;
import com.orion.ops.module.infra.entity.vo.DictKeyVO;

import java.util.List;
import java.util.Map;

/**
 * 字典配置项 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
public interface DictKeyService {

    /**
     * 创建字典配置项
     *
     * @param request request
     * @return id
     */
    Long createDictKey(DictKeyCreateRequest request);

    /**
     * 更新字典配置项
     *
     * @param request request
     * @return effect
     */
    Integer updateDictKeyById(DictKeyUpdateRequest request);

    /**
     * 查询全部字典配置项
     *
     * @return rows
     */
    List<DictKeyVO> getDictKeyList();

    /**
     * 分页 查询字典配置项
     *
     * @param request request
     * @return rows
     */
    DataGrid<DictKeyVO> getDictKeyPage(DictKeyQueryRequest request);

    /**
     * 查询字典配置项 schema
     *
     * @param key key
     * @return schema
     */
    Map<String, String> getDictSchema(String key);

    /**
     * 删除字典配置项
     *
     * @param id id
     * @return effect
     */
    Integer deleteDictKeyById(Long id);

    /**
     * 批量删除字典配置项
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteDictKeyByIdList(List<Long> idList);

}
