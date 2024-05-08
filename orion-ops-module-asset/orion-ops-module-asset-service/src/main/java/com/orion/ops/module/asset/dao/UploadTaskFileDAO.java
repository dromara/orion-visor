package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.UploadTaskFileDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 上传任务文件 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-8 10:31
 */
@Mapper
public interface UploadTaskFileDAO extends IMapper<UploadTaskFileDO> {

}
