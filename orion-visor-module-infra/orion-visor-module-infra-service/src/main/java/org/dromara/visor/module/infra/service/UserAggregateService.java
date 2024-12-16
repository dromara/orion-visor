package org.dromara.visor.module.infra.service;

import org.dromara.visor.module.infra.entity.vo.SystemMenuVO;
import org.dromara.visor.module.infra.entity.vo.UserAggregateVO;

import java.util.List;

/**
 * 用户聚合服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/16 14:41
 */
public interface UserAggregateService {

    /**
     * 获取用户菜单
     *
     * @return menu
     */
    List<SystemMenuVO> getUserMenuList();

    /**
     * 获取用户权限聚合信息
     *
     * @return UserAggregate
     */
    UserAggregateVO getUserAggregateInfo();

}
