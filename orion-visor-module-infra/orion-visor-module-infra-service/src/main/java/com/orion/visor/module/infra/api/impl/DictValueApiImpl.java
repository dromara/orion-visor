package com.orion.visor.module.infra.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.orion.visor.module.infra.api.DictValueApi;
import com.orion.visor.module.infra.service.DictValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 字典配置值 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/4 18:55
 */
@Slf4j
@Service
public class DictValueApiImpl implements DictValueApi {

    @Resource
    private DictValueService dictValueService;

    @Override
    public List<JSONObject> getDictValue(String key) {
        return dictValueService.getDictValue(key);
    }

    @Override
    public Map<String, List<JSONObject>> getDictValueList(List<String> keys) {
        return dictValueService.getDictValueList(keys);
    }

}
