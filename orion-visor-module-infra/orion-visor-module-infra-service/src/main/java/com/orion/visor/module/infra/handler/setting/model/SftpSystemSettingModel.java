package com.orion.visor.module.infra.handler.setting.model;

import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SFTP 系统配置模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/9 11:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SftpSystemSettingModel implements GenericsDataModel {

    /**
     * 预览大小
     */
    private Integer previewSize;

}
