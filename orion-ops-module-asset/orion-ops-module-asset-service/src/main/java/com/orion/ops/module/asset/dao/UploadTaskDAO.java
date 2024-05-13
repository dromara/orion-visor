package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.UploadTaskDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 上传任务 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Mapper
public interface UploadTaskDAO extends IMapper<UploadTaskDO> {

}
