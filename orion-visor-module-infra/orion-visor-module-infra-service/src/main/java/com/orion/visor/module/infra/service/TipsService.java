package com.orion.visor.module.infra.service;

import java.util.List;

/**
 * 提示服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 17:53
 */
public interface TipsService {

    /**
     * 设置为已提示
     *
     * @param tippedKey tippedKey
     */
    void tipped(String tippedKey);

    /**
     * 获取所有已提示的 key
     *
     * @return keys
     */
    List<String> getTippedKeys();

}
