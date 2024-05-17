package com.orion.visor.module.infra.api;

import com.orion.visor.module.infra.enums.FavoriteTypeEnum;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 收藏 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
public interface FavoriteApi {

    /**
     * 获取收藏 relId 列表 会有已删除的 id
     *
     * @param type   type
     * @param userId userId
     * @return relIdList
     */
    List<Long> getFavoriteRelIdList(FavoriteTypeEnum type, Long userId);

    /**
     * 异步查询用户收藏
     *
     * @param type   type
     * @param userId userId
     * @return relIdList
     */
    Future<List<Long>> getFavoriteRelIdListAsync(FavoriteTypeEnum type, Long userId);

    /**
     * 通过 relId 删除收藏
     *
     * @param type  type
     * @param relId relId
     */
    void deleteByRelId(FavoriteTypeEnum type, Long relId);

    /**
     * 通过 relId 删除收藏
     *
     * @param type      type
     * @param relIdList relIdList
     */
    void deleteByRelIdList(FavoriteTypeEnum type, List<Long> relIdList);

}
