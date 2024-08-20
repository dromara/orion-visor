package com.orion.visor.module.infra.api.impl;

import com.orion.visor.module.infra.api.SystemUserApi;
import com.orion.visor.module.infra.convert.SystemUserProviderConvert;
import com.orion.visor.module.infra.dao.SystemUserDAO;
import com.orion.visor.module.infra.entity.domain.SystemUserDO;
import com.orion.visor.module.infra.entity.dto.user.SystemUserDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 15:15
 */
@Service
public class SystemUserApiImpl implements SystemUserApi {

    @Resource
    private SystemUserDAO systemUserDAO;

    @Override
    public String getUsernameById(Long id) {
        return systemUserDAO.of()
                .createWrapper()
                .select(SystemUserDO::getUsername)
                .eq(SystemUserDO::getId, id)
                .then()
                .getOne(SystemUserDO::getUsername);
    }

    @Override
    public String getNicknameById(Long id) {
        return systemUserDAO.of()
                .createWrapper()
                .select(SystemUserDO::getNickname)
                .eq(SystemUserDO::getId, id)
                .then()
                .getOne(SystemUserDO::getNickname);
    }

    @Override
    public SystemUserDTO getUserById(Long id) {
        SystemUserDO user = systemUserDAO.selectById(id);
        if (user == null) {
            return null;
        }
        return SystemUserProviderConvert.MAPPER.to(user);
    }

}
