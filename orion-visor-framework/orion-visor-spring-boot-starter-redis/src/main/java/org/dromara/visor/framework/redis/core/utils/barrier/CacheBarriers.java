/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.framework.redis.core.utils.barrier;

import cn.orionsec.kit.lang.define.barrier.GenericsAnonymousCollectionBarrier;
import cn.orionsec.kit.lang.define.barrier.GenericsAnonymousMapBarrier;
import cn.orionsec.kit.lang.define.barrier.GenericsBarrier;
import cn.orionsec.kit.lang.define.cache.key.model.LongCacheIdModel;
import cn.orionsec.kit.lang.utils.collect.Lists;
import org.dromara.visor.framework.common.constant.Const;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 缓存屏障工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/15 1:22
 */
public class CacheBarriers {

    private CacheBarriers() {
    }

    public static final GenericsBarrier<Collection<?>> LIST = GenericsAnonymousCollectionBarrier.create(Const.NONE_ID);

    public static final GenericsBarrier<Map<?, ?>> MAP = GenericsAnonymousMapBarrier.create(Const.NONE_ID, Const.NONE_ID);

    /**
     * 创建屏障对象 防止穿透
     *
     * @param supplier supplier
     * @param <T>      T
     * @return barrier
     */
    public static <T extends LongCacheIdModel> T createBarrier(Supplier<T> supplier) {
        T val = supplier.get();
        val.setId(Const.NONE_ID);
        return val;
    }

    /**
     * 检测是否需要添加屏障对象 防止穿透
     *
     * @param list     list
     * @param supplier supplier
     * @param <T>      T
     */
    public static <T extends LongCacheIdModel> void checkBarrier(Collection<T> list, Supplier<T> supplier) {
        if (list != null && list.isEmpty()) {
            // 添加屏障对象
            list.add(createBarrier(supplier));
        }
    }

    /**
     * 移除屏障对象
     *
     * @param list list
     * @param <T>  T
     */
    public static <T extends LongCacheIdModel> void removeBarrier(Collection<T> list) {
        if (!Lists.isEmpty(list)) {
            list.removeIf(s -> Const.NONE_ID.equals(s.getId()));
        }
    }

}

