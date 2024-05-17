package com.orion.visor.module.infra.api.impl;

import com.orion.visor.module.infra.api.FileUploadApi;
import com.orion.visor.module.infra.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 文件上传 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 19:04
 */
@Slf4j
@Service
public class FileUploadApiImpl implements FileUploadApi {

    @Resource
    private FileUploadService fileUploadService;

    @Override
    public String createUploadToken(Long userId, String endpoint) {
        return fileUploadService.createUploadToken(userId, endpoint);
    }

}
