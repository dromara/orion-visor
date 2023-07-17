package com.orion.ops.module.infra.service.impl;

import com.orion.ops.module.infra.dao.SystemRoleMenuDAO;
import com.orion.ops.module.infra.service.SystemRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 角色菜单关联 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:46
 */
@Slf4j
@Service
public class SystemRoleMenuServiceImpl implements SystemRoleMenuService {

    @Resource
    private SystemRoleMenuDAO systemRoleMenuDAO;

}
