/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.asset.convert.UploadTaskFileConvert;
import org.dromara.visor.module.asset.dao.UploadTaskFileDAO;
import org.dromara.visor.module.asset.entity.domain.UploadTaskFileDO;
import org.dromara.visor.module.asset.entity.vo.UploadTaskFileVO;
import org.dromara.visor.module.asset.service.UploadTaskFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 上传任务文件 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-8 10:31
 */
@Slf4j
@Service
public class UploadTaskFileServiceImpl implements UploadTaskFileService {

    @Resource
    private UploadTaskFileDAO uploadTaskFileDAO;

    @Override
    public List<UploadTaskFileVO> getFileByTaskId(Long taskId) {
        // 查询
        return uploadTaskFileDAO.selectByTaskId(taskId)
                .stream()
                .map(UploadTaskFileConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Integer deleteFileByTaskId(Long taskId) {
        log.info("UploadTaskFileService-deleteFileByTaskId id: {}", taskId);
        LambdaQueryWrapper<UploadTaskFileDO> wrapper = uploadTaskFileDAO.wrapper()
                .eq(UploadTaskFileDO::getTaskId, taskId);
        int effect = uploadTaskFileDAO.delete(wrapper);
        log.info("UploadTaskFileService-deleteFileByTaskId id: {}, effect: {}", taskId, effect);
        return effect;
    }

    @Override
    public Integer deleteFileByTaskIdList(List<Long> taskIdList) {
        log.info("UploadTaskFileService-deleteFileByTaskIdList idList: {}", taskIdList);
        LambdaQueryWrapper<UploadTaskFileDO> wrapper = uploadTaskFileDAO.wrapper()
                .in(UploadTaskFileDO::getTaskId, taskIdList);
        int effect = uploadTaskFileDAO.delete(wrapper);
        log.info("UploadTaskFileService-deleteFileByTaskIdList effect: {}", effect);
        return effect;
    }

}
