package com.orion.visor.module.asset.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.visor.module.asset.entity.dto.HostTerminalAccessDTO;
import com.orion.visor.module.asset.entity.dto.HostTerminalTransferDTO;
import com.orion.visor.module.asset.entity.dto.SftpGetContentCacheDTO;
import com.orion.visor.module.asset.entity.dto.SftpSetContentCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 主机终端服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/27 18:13
 */
public interface HostTerminalCacheKeyDefine {

    CacheKeyDefine HOST_TERMINAL_ACCESS = new CacheKeyBuilder()
            .key("host:terminal:access:{}")
            .desc("主机终端访问token ${token}")
            .type(HostTerminalAccessDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(3, TimeUnit.MINUTES)
            .build();

    CacheKeyDefine HOST_TERMINAL_TRANSFER = new CacheKeyBuilder()
            .key("host:terminal:transfer:{}")
            .desc("主机终端传输token ${token}")
            .type(HostTerminalTransferDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(3, TimeUnit.MINUTES)
            .build();

    CacheKeyDefine SFTP_GET_CONTENT = new CacheKeyBuilder()
            .key("sftp:get:content:{}")
            .desc("sftp 获取文件内容 ${token}")
            .type(SftpGetContentCacheDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(5, TimeUnit.MINUTES)
            .build();

    CacheKeyDefine SFTP_SET_CONTENT = new CacheKeyBuilder()
            .key("sftp:set:content:{}")
            .desc("sftp 设置文件内容 ${token}")
            .type(SftpSetContentCacheDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(5, TimeUnit.MINUTES)
            .build();

}
