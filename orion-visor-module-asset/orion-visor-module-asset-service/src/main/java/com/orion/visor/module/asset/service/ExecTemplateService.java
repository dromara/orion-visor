package com.orion.visor.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.asset.entity.request.exec.ExecTemplateCreateRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecTemplateQueryRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecTemplateUpdateRequest;
import com.orion.visor.module.asset.entity.vo.ExecTemplateVO;

/**
 * 执行模板 服务类
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
public interface ExecTemplateService {

    /**
     * 创建执行模板
     *
     * @param request request
     * @return id
     */
    Long createExecTemplate(ExecTemplateCreateRequest request);

    /**
     * 更新执行模板
     *
     * @param request request
     * @return effect
     */
    Integer updateExecTemplateById(ExecTemplateUpdateRequest request);

    /**
     * 查询执行模板
     *
     * @param id id
     * @return row
     */
    ExecTemplateVO getExecTemplateById(Long id);

    /**
     * 查询执行模板 (查询认证的主机)
     *
     * @param id id
     * @return row
     */
    ExecTemplateVO getExecTemplateWithAuthorized(Long id);

    /**
     * 分页查询执行模板
     *
     * @param request request
     * @return rows
     */
    DataGrid<ExecTemplateVO> getExecTemplatePage(ExecTemplateQueryRequest request);

    /**
     * 删除执行模板
     *
     * @param id id
     * @return effect
     */
    Integer deleteExecTemplateById(Long id);

}
