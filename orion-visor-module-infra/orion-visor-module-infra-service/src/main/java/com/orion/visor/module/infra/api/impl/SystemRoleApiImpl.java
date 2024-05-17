package com.orion.visor.module.infra.api.impl;

import com.orion.visor.module.infra.api.SystemRoleApi;
import com.orion.visor.module.infra.convert.SystemRoleProviderConvert;
import com.orion.visor.module.infra.dao.SystemRoleDAO;
import com.orion.visor.module.infra.entity.domain.SystemRoleDO;
import com.orion.visor.module.infra.entity.dto.role.SystemRoleDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 角色服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 15:16
 */
@Service
public class SystemRoleApiImpl implements SystemRoleApi {

    @Resource
    private SystemRoleDAO systemRoleDAO;

    @Override
    public SystemRoleDTO getRoleById(Long id) {
        SystemRoleDO role = systemRoleDAO.selectById(id);
        if (role == null) {
            return null;
        }
        return SystemRoleProviderConvert.MAPPER.to(role);
    }

}
