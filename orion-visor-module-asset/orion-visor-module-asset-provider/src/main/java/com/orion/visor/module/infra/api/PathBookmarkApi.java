package com.orion.visor.module.infra.api;

import java.util.List;

/**
 * 路径标签 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/3 11:07
 */
public interface PathBookmarkApi {

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    Integer deleteByUserIdList(List<Long> userIdList);

}
