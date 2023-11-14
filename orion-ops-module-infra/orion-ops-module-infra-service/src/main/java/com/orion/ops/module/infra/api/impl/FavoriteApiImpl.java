package com.orion.ops.module.infra.api.impl;

import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.api.FavoriteApi;
import com.orion.ops.module.infra.dao.FavoriteDAO;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.ops.module.infra.enums.FavoriteTypeEnum;
import com.orion.ops.module.infra.service.FavoriteService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 收藏 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
@Service
public class FavoriteApiImpl implements FavoriteApi {

    @Resource
    private FavoriteService favoriteService;

    @Resource
    private FavoriteDAO favoriteDAO;

    @Override
    public List<Long> getFavoriteRelIdList(FavoriteTypeEnum type, Long userId) {
        Valid.allNotNull(type, userId);
        // 查询
        FavoriteQueryRequest request = new FavoriteQueryRequest();
        request.setType(type.name());
        request.setUserId(userId);
        return favoriteService.getFavoriteRelIdList(request);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteByRelIdAsync(FavoriteTypeEnum type, Long relId) {
        Valid.allNotNull(type, relId);
        favoriteDAO.deleteFavoriteByRelId(type.name(), relId);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteByRelIdListAsync(FavoriteTypeEnum type, List<Long> relIdList) {
        Valid.notNull(type);
        Valid.notEmpty(relIdList);
        favoriteDAO.deleteFavoriteByRelIdList(type.name(), relIdList);
    }

}
