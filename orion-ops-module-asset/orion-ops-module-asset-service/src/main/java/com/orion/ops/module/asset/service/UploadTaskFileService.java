package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.vo.UploadTaskFileVO;

import java.util.List;

/**
 * 上传任务文件 服务类
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-8 10:31
 */
public interface UploadTaskFileService {

    /**
     * 查询上传任务文件
     *
     * @param taskId taskId
     * @return row
     */
    List<UploadTaskFileVO> getFileByTaskId(Long taskId);

    /**
     * 通过 taskId 删除
     *
     * @param taskId taskId
     * @return effect
     */
    Integer deleteFileByTaskId(Long taskId);

    /**
     * 通过 taskId 删除
     *
     * @param taskIdList taskIdList
     * @return effect
     */
    Integer deleteFileByTaskIdList(List<Long> taskIdList);

}
