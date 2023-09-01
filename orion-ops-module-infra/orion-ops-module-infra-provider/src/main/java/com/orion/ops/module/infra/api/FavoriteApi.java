package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.enums.FavoriteTypeEnum;

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
     * 添加收藏
     *
     * @param type   type
     * @param userId userId
     * @param relId  relId
     */
    void addFavorite(FavoriteTypeEnum type, Long userId, Long relId);

    /**
     * 获取收藏 relId 列表 会有已删除的 id
     *
     * @param type   type
     * @param userId userId
     * @return relIdList
     */
    Future<List<Long>> getFavoriteRelIdList(FavoriteTypeEnum type, Long userId);

    /**
     * 通过 userId 删除收藏
     *
     * @param userId userId
     */
    void deleteFavoriteByUserId(Long userId);

    /**
     * 通过 userId 删除收藏
     *
     * @param userIdList userId
     */
    void deleteFavoriteByUserIdList(List<Long> userIdList);

    /**
     * 通过 relId 删除收藏
     *
     * @param relId relId
     */
    void deleteFavoriteByRelId(Long relId);

    /**
     * 通过 relId 删除收藏
     *
     * @param relIdList relIdList
     */
    void deleteFavoriteByRelIdList(List<Long> relIdList);

}
