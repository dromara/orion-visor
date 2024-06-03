package com.orion.visor.module.infra.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.infra.entity.request.user.*;
import com.orion.visor.module.infra.entity.vo.SystemUserVO;

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
    Integer updateSystemUserById(SystemUserUpdateRequest request);

    /**
     * 修改用户状态
     *
     * @param request request
     * @return effect
     */
    Integer updateUserStatus(SystemUserUpdateStatusRequest request);

    /**
     * 通过 id 查询用户
     *
     * @param id id
     * @return row
     */
    SystemUserVO getSystemUserById(Long id);

    /**
     * 查询所有用户
     *
     * @return rows
     */
    List<SystemUserVO> getSystemUserList();

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
    Integer deleteSystemUserById(Long id);

    /**
     * 通过 id 批量删除用户
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteSystemUserByIdList(List<Long> idList);

    /**
     * 通过 idList 删除用户拓展信息
     *
     * @param idList idList
     */
    void deleteSystemUserListRelAsync(List<Long> idList);

    /**
     * 重置密码
     *
     * @param request request
     */
    void resetPassword(UserResetPasswordRequest request);

    /**
     * 检测用户是否是为管理员
     *
     * @param userId userId
     * @return 是否为管理员
     */
    boolean isAdminUser(Long userId);

}
