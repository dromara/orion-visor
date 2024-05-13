package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.UploadTaskFileDO;
import com.orion.ops.module.asset.entity.vo.UploadTaskFileVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 上传任务文件 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-8 10:31
 */
@Mapper
public interface UploadTaskFileConvert {

    UploadTaskFileConvert MAPPER = Mappers.getMapper(UploadTaskFileConvert.class);

    UploadTaskFileVO to(UploadTaskFileDO domain);

    List<UploadTaskFileVO> to(List<UploadTaskFileDO> list);

}
