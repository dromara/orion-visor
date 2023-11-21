package com.orion.ops.framework.redis.core.utils.barrier;

import com.orion.lang.utils.collect.Lists;

import java.util.Collection;

/**
 * 标准集合屏障
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/21 11:46
 */
public class GenericsListBarrier<T> {

    private final T barrierValue;

    public GenericsListBarrier(T barrierValue) {
        this.barrierValue = barrierValue;
    }

    /**
     * 创建屏障
     *
     * @param barrierValue barrierValue
     * @param <T>          T
     * @return barrier
     */
    public static <T> GenericsListBarrier<T> create(T barrierValue) {
        return new GenericsListBarrier<>(barrierValue);
    }


    /**
     * 检测是否需要添加屏障对象 防止穿透
     *
     * @param list list
     */
    public void check(Collection<T> list) {
        if (list != null && list.isEmpty()) {
            // 添加屏障对象
            list.add(barrierValue);
        }
    }

    /**
     * 移除屏障对象
     *
     * @param list list
     */
    public void remove(Collection<T> list) {
        if (!Lists.isEmpty(list)) {
            list.removeIf(s -> s.equals(barrierValue));
        }
    }

}
