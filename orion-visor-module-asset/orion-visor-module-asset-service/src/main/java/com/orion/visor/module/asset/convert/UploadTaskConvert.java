package com.orion.visor.module.asset.convert;

import com.orion.visor.module.asset.entity.domain.UploadTaskDO;
import com.orion.visor.module.asset.entity.request.upload.UploadTaskCreateRequest;
import com.orion.visor.module.asset.entity.request.upload.UploadTaskQueryRequest;
import com.orion.visor.module.asset.entity.vo.UploadTaskStatusVO;
import com.orion.visor.module.asset.entity.vo.UploadTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 上传任务 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Mapper
public interface UploadTaskConvert {

    UploadTaskConvert MAPPER = Mappers.getMapper(UploadTaskConvert.class);

    UploadTaskDO to(UploadTaskCreateRequest request);

    UploadTaskDO to(UploadTaskQueryRequest request);

    UploadTaskVO to(UploadTaskDO domain);

    List<UploadTaskVO> toList(List<UploadTaskDO> list);

    UploadTaskStatusVO toStatus(UploadTaskDO domain);

}
