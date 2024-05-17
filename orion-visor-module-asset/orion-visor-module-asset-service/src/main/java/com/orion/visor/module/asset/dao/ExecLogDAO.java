package com.orion.visor.module.asset.dao;

import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.ExecLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批量执行日志 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Mapper
public interface ExecLogDAO extends IMapper<ExecLogDO> {

    /**
     * 通过 id 和 source 查询
     *
     * @param id     id
     * @param source source
     * @return log
     */
    default ExecLogDO selectByIdSource(Long id, String source) {
        return this.of()
                .createWrapper()
                .eq(ExecLogDO::getId, id)
                .eq(ExecLogDO::getSource, source)
                .then()
                .getOne();
    }

    /**
     * 获取执行历史
     *
     * @param source source
     * @param userId userId
     * @param limit  limit
     * @return rows
     */
    List<ExecLogDO> getExecHistory(@Param("source") String source,
                                   @Param("userId") Long userId,
                                   @Param("limit") Integer limit);

}
