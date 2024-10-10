package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.request.system.SystemSettingUpdatePartialRequest;
import com.orion.visor.module.infra.entity.request.system.SystemSettingUpdateRequest;
import com.orion.visor.module.infra.entity.vo.AppInfoVO;

import java.util.Map;

/**
 * 系统设置服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/16 0:18
 */
public interface SystemSettingService {

    /**
     * 获取应用信息
     *
     * @return info
     */
    AppInfoVO getAppInfo();

    /**
     * 更新系统设置
     *
     * @param request request
     * @return effect
     */
    Integer updateSystemSetting(SystemSettingUpdateRequest request);

    /**
     * 更新部分系统设置
     *
     * @param request request
     */
    void updatePartialSystemSetting(SystemSettingUpdatePartialRequest request);

    /**
     * 通过类型查询系统设置
     *
     * @param type type
     * @return row
     */
    Map<String, Object> getSystemSettingByType(String type);

}
