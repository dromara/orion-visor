package com.orion.visor.module.asset.api.impl;

import com.orion.visor.module.asset.service.PathBookmarkService;
import com.orion.visor.module.infra.api.PathBookmarkApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 路径标签 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/3 11:11
 */
@Slf4j
@Service
public class PathBookmarkApiImpl implements PathBookmarkApi {

    @Resource
    private PathBookmarkService pathBookmarkService;

    @Override
    public Integer deleteByUserIdList(List<Long> userIdList) {
        return pathBookmarkService.deleteByUserIdList(userIdList);
    }

}
