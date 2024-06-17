package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.vo.AppInfoVO;

/**
 * 系统服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/17 18:10
 */
public interface SystemSettingService {

    /**
     * 获取应用信息
     *
     * @return info
     */
    AppInfoVO getAppInfo();

}
