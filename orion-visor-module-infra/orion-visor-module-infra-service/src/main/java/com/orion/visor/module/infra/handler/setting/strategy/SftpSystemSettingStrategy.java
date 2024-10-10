package com.orion.visor.module.infra.handler.setting.strategy;

import com.orion.lang.utils.Exceptions;
import com.orion.visor.framework.common.handler.data.strategy.AbstractGenericsDataStrategy;
import com.orion.visor.module.infra.handler.setting.model.SftpSystemSettingModel;
import org.springframework.stereotype.Component;

/**
 * SFTP 系统配置策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/9 11:44
 */
@Component
public class SftpSystemSettingStrategy extends AbstractGenericsDataStrategy<SftpSystemSettingModel> {

    public SftpSystemSettingStrategy() {
        super(SftpSystemSettingModel.class);
    }

    @Override
    public SftpSystemSettingModel getDefault() {
        return SftpSystemSettingModel.builder()
                .previewSize(2)
                .build();
    }

    @Override
    public SftpSystemSettingModel parse(String serialModel) {
        throw Exceptions.unsupported();
    }

}