package com.orion.visor.module.infra.service.impl;

import com.orion.ext.process.ProcessAwaitExecutor;
import com.orion.lang.support.Attempt;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.lang.utils.io.Streams;
import com.orion.visor.framework.common.constant.AppConst;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.module.infra.entity.vo.AppInfoVO;
import com.orion.visor.module.infra.service.SystemSettingService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

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
        String[][] cmd = new String[][]{
                new String[]{"/bin/sh", "-c", "cat /sys/class/dmi/id/product_serial"},
                new String[]{"/bin/bash", "-c", "cat /sys/class/dmi/id/product_serial"},
                new String[]{"/bin/sh", "-c", "dmidecode -s system-uuid"},
                new String[]{"/bin/bash", "-c", "dmidecode -s system-uuid"},
                new String[]{"cmd", "/c", "wmic csproduct get uuid"}
        };
        for (String[] s : cmd) {
            try {
                String uuid = this.getCommandOutput(s);
                if (Strings.isBlank(uuid)) {
                    continue;
                }
                // 去除符号并且转为大写
                uuid = uuid.replaceAll(Const.DASHED, Const.EMPTY)
                        .toUpperCase()
                        .trim();
                // 去除 \n
                String extraUuid = Arrays1.last(uuid.trim().split(Const.LF));
                if (!Strings.isBlank(extraUuid)) {
                    uuid = extraUuid.trim();
                }
                // 去除 :
                extraUuid = Arrays1.last(uuid.trim().split(Const.COLON));
                if (!Strings.isBlank(extraUuid)) {
                    uuid = extraUuid.trim();
                }
                return this.uuid = Signatures.md5(uuid);
            } catch (Exception e) {
                // IGNORED
            }
        }
        return this.uuid = Const.UNKNOWN;
    }

    /**
     * 获取输出结果
     *
     * @param command command
     * @return result
     */
    private String getCommandOutput(String[] command) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(command);
        try {
            executor.streamHandler(i -> Attempt.uncheck(Streams::transfer, i, out))
                    .waitFor()
                    .sync()
                    .exec();
            return out.toString();
        } finally {
            Streams.close(out);
            Streams.close(executor);
        }
    }

}
