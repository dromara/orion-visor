package com.orion.visor.module.asset.entity.dto;

import com.orion.visor.module.asset.entity.vo.HostBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 上传任务拓展信息对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 23:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UploadTaskExtraDTO", description = "上传任务拓展信息对象")
public class UploadTaskExtraDTO {

    @Schema(description = "hostIdList")
    private List<Long> hostIdList;

    @Schema(description = "主机信息")
    private List<HostBaseVO> hosts;

}
