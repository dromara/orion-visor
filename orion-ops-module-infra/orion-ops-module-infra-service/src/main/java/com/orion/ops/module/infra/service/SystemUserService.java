package com.orion.ops.module.infra.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.entity.request.*;
import com.orion.ops.module.infra.convert.*;

import java.util.List;

/**
 * 用户 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
public interface SystemUserService {

    /**
     * 创建用户
     *
     * @param request request
     * @return id
     */
    Long createSystemUser(SystemUserCreateRequest request);

    /**
     * 通过 id 更新用户
     *
     * @param request request
     * @return effect
     */
    Integer updateSystemUser(SystemUserUpdateRequest request);

    /**
     * 通过 id 查询用户
     *
     * @param id id
     * @return row
     */
    SystemUserVO getSystemUser(Long id);

    /**
     * 通过 id 批量查询用户
     *
     * @param idList idList
     * @return rows
     */
    List<SystemUserVO> getSystemUserList(List<Long> idList);

    /**
     * 分页查询用户
     *
     * @param request request
     * @return rows
     */
    DataGrid<SystemUserVO> getSystemUserPage(SystemUserQueryRequest request);

    /**
     * 通过 id 删除用户
     *
     * @param id id
     * @return effect
     */
    Integer deleteSystemUser(Long id);

    /**
     * 通过 id 批量删除用户
     *
     * @param idList idList
     * @return effect
     */
    Integer batchDeleteSystemUser(List<Long> idList);

}
