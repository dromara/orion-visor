package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskCreateRequest;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskQueryRequest;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskRequest;
import com.orion.ops.module.asset.entity.vo.UploadTaskCreateVO;
import com.orion.ops.module.asset.entity.vo.UploadTaskStatusVO;
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

    String SWAP_ENDPOINT = "/upload/swap/{}";

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
    UploadTaskVO getUploadTask(Long id);

    /**
     * 分页查询上传任务
     *
     * @param request request
     * @return rows
     */
    DataGrid<UploadTaskVO> getUploadTaskPage(UploadTaskQueryRequest request);

    /**
     * 获取上传任务状态
     *
     * @param idList     idList
     * @param queryFiles queryFiles
     * @return rows
     */
    List<UploadTaskStatusVO> getUploadTaskStatus(List<Long> idList, Boolean queryFiles);

    /**
     * 获取上传任务数量
     *
     * @param request request
     * @return count
     */
    Long getUploadTaskCount(UploadTaskQueryRequest request);

    /**
     * 清理上传任务
     *
     * @param request request
     * @return count
     */
    Integer clearUploadTask(UploadTaskQueryRequest request);

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

    /**
     * 开始上传
     *
     * @param id id
     */
    void startUploadTask(Long id);

    /**
     * 取消上传
     *
     * @param request request
     */
    void cancelUploadTask(UploadTaskRequest request);

    /**
     * 删除上传交换区的文件
     *
     * @param id id
     */
    void clearUploadSwapFiles(Long id);

    /**
     * 删除上传交换区的文件
     *
     * @param idList idList
     */
    void clearUploadSwapFiles(List<Long> idList);

}
