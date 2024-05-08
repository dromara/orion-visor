package com.orion.ops.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.framework.mybatis.core.query.Conditions;
import com.orion.ops.module.asset.entity.domain.UploadTaskFileDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 上传任务文件 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-8 10:31
 */
@Mapper
public interface UploadTaskFileDAO extends IMapper<UploadTaskFileDO> {

    /**
     * 通过 taskId 查询
     *
     * @param taskId taskId
     * @return files
     */
    default List<UploadTaskFileDO> selectByTaskId(Long taskId) {
        return this.selectList(Conditions.eq(UploadTaskFileDO::getTaskId, taskId));
    }

    /**
     * 通过 taskId hostId 更新状态
     *
     * @param taskId taskId
     * @param hostId hostId
     * @param status status
     * @return effect
     */
    default int updateStatusByTaskHostId(Long taskId, Long hostId, String status) {
        // 条件
        LambdaQueryWrapper<UploadTaskFileDO> wrapper = this.wrapper()
                .eq(UploadTaskFileDO::getTaskId, taskId)
                .eq(UploadTaskFileDO::getHostId, hostId);
        // 修改值
        UploadTaskFileDO update = new UploadTaskFileDO();
        update.setStatus(status);
        // 更新
        return this.update(update, wrapper);
    }

    /**
     * 通过 id 更新状态
     *
     * @param idList idList
     * @param status status
     * @return effect
     */
    default int updateStatusByIdList(List<Long> idList, String status) {
        UploadTaskFileDO update = new UploadTaskFileDO();
        update.setStatus(status);
        // 更新
        return this.update(update, Conditions.in(UploadTaskFileDO::getId, idList));
    }

}
