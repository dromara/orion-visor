package com.orion.visor.module.infra.service.impl;

import com.orion.ext.process.Processes;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.visor.framework.common.constant.AppConst;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.utils.Mixes;
import com.orion.visor.module.infra.entity.vo.AppInfoVO;
import com.orion.visor.module.infra.service.SystemSettingService;
import org.springframework.stereotype.Service;

/**
 * 系统服务 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/17 18:10
 */
@Service
public class SystemSettingServiceImpl implements SystemSettingService {

    private String uuid;

    @Override
    public AppInfoVO getAppInfo() {
        return AppInfoVO.builder()
                .version(AppConst.VERSION)
                .uuid(this.getSystemUuid())
                .build();
    }

    /**
     * 获取系统 uuid
     *
     * @return uuid
     */
    private String getSystemUuid() {
        if (this.uuid != null) {
            return this.uuid;
        }
        String[] cmd = new String[]{"cat /sys/class/dmi/id/product_serial", "dmidecode -s system-uuid", "wmic csproduct get uuid"};
        for (String s : cmd) {
            try {
                // 执行命令获取 uuid
                String uuid = Processes.getOutputResultString(s);
                if (Strings.isBlank(uuid)) {
                    continue;
                }
                // 去除符号并且转为大写
                uuid = uuid.replaceAll(Const.DASHED, Const.EMPTY).toUpperCase();
                // 去除特殊字符
                String extraUuid = Arrays1.last(uuid.trim().split(Const.LF));
                if (!Strings.isBlank(extraUuid)) {
                    uuid = extraUuid;
                }
                // 转义
                return this.uuid = Mixes.obfuscate(uuid);
            } catch (Exception e) {
                // IGNORED
            }
        }
        return this.uuid = Const.UNKNOWN;
    }

}
