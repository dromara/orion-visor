/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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

import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.collect.Maps;
import com.alibaba.fastjson.JSONObject;
import org.dromara.visor.common.entity.chart.PieChartData;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.asset.dao.HostConfigDAO;
import org.dromara.visor.module.asset.define.cache.AssetStatisticsCacheKeyDefine;
import org.dromara.visor.module.asset.entity.po.HostTypeCountPO;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.dromara.visor.module.asset.service.AssetStatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产模块统计服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/7 16:26
 */
@Service
public class AssetStatisticsServiceImpl implements AssetStatisticsService {

    @Resource
    private HostConfigDAO hostConfigDAO;

    @Override
    public PieChartData getHostTypeChart() {
        // 查询缓存
        JSONObject cache = RedisStrings.getJson(AssetStatisticsCacheKeyDefine.HOST_TYPE_COUNT);
        if (Maps.isEmpty(cache)) {
            cache = new JSONObject();
            // 查询数据库
            List<HostTypeCountPO> typeCountList = hostConfigDAO.selectEnabledTypeCount();
            for (HostTypeCountPO typeCount : typeCountList) {
                cache.put(typeCount.getType(), typeCount.getCount());
            }
            // 设置屏障 防止穿透
            CacheBarriers.STRING_MAP.check(cache);
            // 设置缓存
            RedisStrings.set(AssetStatisticsCacheKeyDefine.HOST_TYPE_COUNT, cache);
        }
        // 删除屏障
        CacheBarriers.STRING_MAP.remove(cache);
        // 查询类型数量
        Map<String, Integer> data = new HashMap<>();
        for (HostTypeEnum value : HostTypeEnum.values()) {
            data.put(value.name(), Objects1.def(cache.getInteger(value.name()), 0));
        }
        return new PieChartData(data);
    }

}
