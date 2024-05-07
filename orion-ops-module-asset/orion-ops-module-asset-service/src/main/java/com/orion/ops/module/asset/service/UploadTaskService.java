package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskCreateRequest;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskQueryRequest;
import com.orion.ops.module.asset.entity.vo.UploadTaskCreateVO;
import com.orion.ops.module.asset.entity.vo.UploadTaskVO;

import java.util.List;

/**
 * 上传任务 服务类
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
public interface UploadTaskService {

    /**
     * 创建上传任务
     *
     * @param request request
     * @return result
     */
    UploadTaskCreateVO createUploadTask(UploadTaskCreateRequest request);

    /**
     * 查询上传任务
     *
     * @param id id
     * @return row
     */
    UploadTaskVO getUploadTaskById(Long id);

    /**
     * 分页查询上传任务
     *
     * @param request request
     * @return rows
     */
    DataGrid<UploadTaskVO> getUploadTaskPage(UploadTaskQueryRequest request);

    /**
     * 删除上传任务
     *
     * @param id id
     * @return effect
     */
    Integer deleteUploadTaskById(Long id);

    /**
     * 批量删除上传任务
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteUploadTaskByIdList(List<Long> idList);

}
