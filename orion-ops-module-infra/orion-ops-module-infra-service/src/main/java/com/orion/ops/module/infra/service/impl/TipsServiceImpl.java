package com.orion.ops.module.infra.service.impl;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.redis.core.utils.RedisLists;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.define.cache.TipsCacheKeyDefine;
import com.orion.ops.module.infra.service.TipsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提示 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 17:57
 */
@Service
public class TipsServiceImpl implements TipsService {

    @Override
    public void tipped(String tippedKey) {
        Long userId = SecurityUtils.getLoginUserId();
        String key = TipsCacheKeyDefine.TIPS.format(userId);
        RedisLists.push(key, TipsCacheKeyDefine.TIPS, tippedKey);
    }

    @Override
    public List<String> getTippedKeys() {
        return Lists.def(RedisLists.range(TipsCacheKeyDefine.TIPS.format(SecurityUtils.getLoginUserId())));
    }

}
