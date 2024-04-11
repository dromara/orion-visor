package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.ExecJobDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 计划任务 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Mapper
public interface ExecJobDAO extends IMapper<ExecJobDO> {

    /**
     * 自增 exec_seq
     *
     * @param id id
     */
    void incrExecSeq(@Param("id") Long id);

}
