package com.orion.ops.module.infra.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.infra.entity.domain.HistoryValueDO;
import com.orion.ops.module.infra.entity.request.history.HistoryValueCreateRequest;
import com.orion.ops.module.infra.entity.request.history.HistoryValueQueryRequest;
import com.orion.ops.module.infra.entity.vo.HistoryValueVO;

import java.util.List;

/**
 * 历史归档 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
public interface HistoryValueService {

    /**
     * 创建历史归档
     *
     * @param request request
     * @return id
     */
    Long createHistoryValue(HistoryValueCreateRequest request);

    /**
     * 分页查询历史归档
     *
     * @param request request
     * @return rows
     */
    DataGrid<HistoryValueVO> getHistoryValuePage(HistoryValueQueryRequest request);

    /**
     * 通过 id 查询
     *
     * @param id id
     * @return value
     */
    HistoryValueDO getHistoryById(Long id);

    /**
     * 删除历史归档
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteByRelId(String type, Long relId);

    /**
     * 批量删除历史归档
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(String type, List<Long> relIdList);

}
