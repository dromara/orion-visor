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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.redis.core.utils.RedisUtils;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.infra.convert.DictKeyConvert;
import org.dromara.visor.module.infra.dao.DictKeyDAO;
import org.dromara.visor.module.infra.define.cache.DictCacheKeyDefine;
import org.dromara.visor.module.infra.entity.domain.DictKeyDO;
import org.dromara.visor.module.infra.entity.dto.DictKeyCacheDTO;
import org.dromara.visor.module.infra.entity.dto.DictKeyExtraSchemaDTO;
import org.dromara.visor.module.infra.entity.request.dict.DictKeyCreateRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictKeyQueryRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictKeyUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.DictKeyVO;
import org.dromara.visor.module.infra.enums.DictValueTypeEnum;
import org.dromara.visor.module.infra.service.DictKeyService;
import org.dromara.visor.module.infra.service.DictValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字典配置项 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Slf4j
@Service
public class DictKeyServiceImpl implements DictKeyService {

    @Resource
    private DictKeyDAO dictKeyDAO;

    @Resource
    private DictValueService dictValueService;

    @Override
    public Long createDictKey(DictKeyCreateRequest request) {
        log.info("DictKeyService-createDictKey request: {}", JSON.toJSONString(request));
        Valid.valid(DictValueTypeEnum::of, request.getValueType());
        // 转换
        DictKeyDO record = DictKeyConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDictKeyPresent(record);
        // 插入
        int effect = dictKeyDAO.insert(record);
        Long id = record.getId();
        log.info("DictKeyService-createDictKey id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisUtils.delete(DictCacheKeyDefine.DICT_KEY);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateDictKeyById(DictKeyUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("DictKeyService-updateDictKeyById id: {}, request: {}", id, JSON.toJSONString(request));
        Valid.valid(DictValueTypeEnum::of, request.getValueType());
        // 查询
        DictKeyDO record = dictKeyDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.CONFIG_ABSENT);
        // 转换
        DictKeyDO updateRecord = DictKeyConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDictKeyPresent(updateRecord);
        // 更新
        int effect = dictKeyDAO.updateById(updateRecord);
        // 如果修改了 key 则需要修改 dictValue.key
        if (!Objects1.eq(record.getKeyName(), request.getKeyName())) {
            dictValueService.updateKeyNameByKeyId(id, record.getKeyName(), request.getKeyName());
        }
        // 删除缓存
        RedisUtils.delete(DictCacheKeyDefine.DICT_KEY.getKey(),
                DictCacheKeyDefine.DICT_SCHEMA.format(record.getKeyName()));
        log.info("DictKeyService-updateDictKeyById effect: {}", effect);
        return effect;
    }

    @Override
    public List<DictKeyVO> getDictKeyList() {
        // 查询缓存
        List<DictKeyCacheDTO> list = RedisMaps.valuesJson(DictCacheKeyDefine.DICT_KEY);
        if (list.isEmpty()) {
            // 查询数据库
            list = dictKeyDAO.of().list(DictKeyConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, DictKeyCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(DictCacheKeyDefine.DICT_KEY, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(DictKeyConvert.MAPPER::to)
                .sorted(Comparator.comparing(DictKeyVO::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public DataGrid<DictKeyVO> getDictKeyPage(DictKeyQueryRequest request) {
        // 条件
        LambdaQueryWrapper<DictKeyDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return dictKeyDAO.of(wrapper)
                .page(request)
                .dataGrid(DictKeyConvert.MAPPER::to);
    }

    @Override
    public Map<String, String> getDictSchema(String key) {
        // 查询缓存
        String cacheKey = DictCacheKeyDefine.DICT_SCHEMA.format(key);
        JSONObject cacheResult = RedisStrings.getJson(cacheKey);
        if (cacheResult == null) {
            cacheResult = new JSONObject();
            // 查询数据库
            DictKeyDO dictKey = dictKeyDAO.selectByKey(key);
            if (dictKey == null) {
                return Maps.newMap();
            }
            // 构建缓存数据
            cacheResult.put(Const.VALUE, dictKey.getValueType());
            String extraSchema = dictKey.getExtraSchema();
            if (extraSchema != null) {
                List<DictKeyExtraSchemaDTO> schemas = JSON.parseArray(extraSchema, DictKeyExtraSchemaDTO.class);
                for (DictKeyExtraSchemaDTO schema : schemas) {
                    cacheResult.put(schema.getName(), schema.getType());
                }
            }
            // 设置缓存
            RedisStrings.setJson(cacheKey, DictCacheKeyDefine.DICT_SCHEMA, cacheResult);
        }
        // 返回
        Map<String, String> result = Maps.newMap();
        Set<String> schemaKeys = cacheResult.keySet();
        for (String schemaKey : schemaKeys) {
            result.put(schemaKey, cacheResult.getString(schemaKey));
        }
        return result;
    }

    @Override
    public void refreshCache() {
        RedisUtils.scanKeysDelete(
                // 删除字典配置项 schema
                DictCacheKeyDefine.DICT_SCHEMA.format("*"),
                // 删除字典配置值
                DictCacheKeyDefine.DICT_VALUE.format("*")
        );
        // 删除字典配置项列表
        RedisUtils.delete(DictCacheKeyDefine.DICT_KEY);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteDictKeyById(Long id) {
        return this.deleteDictKeyByIdList(Lists.singleton(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteDictKeyByIdList(List<Long> idList) {
        log.info("DictKeyService-deleteDictKeyByIdList idList: {}", idList);
        // 检查数据是否存在
        List<DictKeyDO> dictKeys = dictKeyDAO.selectBatchIds(idList);
        if (dictKeys.isEmpty()) {
            return 0;
        }
        String keys = dictKeys.stream()
                .map(DictKeyDO::getKeyName)
                .collect(Collectors.joining(Const.COMMA));
        OperatorLogs.add(OperatorLogs.KEY_NAME, keys);
        // 删除配置项
        int effect = dictKeyDAO.deleteBatchIds(idList);
        // 删除配置值
        dictValueService.deleteDictValueByKeyIdList(idList);
        log.info("DictKeyService-deleteDictKeyByIdList effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DictCacheKeyDefine.DICT_KEY, idList);
        List<String> schemaKeys = dictKeys.stream()
                .map(DictKeyDO::getKeyName)
                .map(DictCacheKeyDefine.DICT_SCHEMA::format)
                .collect(Collectors.toList());
        RedisUtils.delete(schemaKeys);
        return effect;
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkDictKeyPresent(DictKeyDO domain) {
        // 构造条件
        LambdaQueryWrapper<DictKeyDO> wrapper = dictKeyDAO.wrapper()
                // 更新时忽略当前记录
                .ne(DictKeyDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(DictKeyDO::getKeyName, domain.getKeyName());
        // 检查是否存在
        boolean present = dictKeyDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<DictKeyDO> buildQueryWrapper(DictKeyQueryRequest request) {
        String searchValue = request.getSearchValue();
        return dictKeyDAO.wrapper()
                .eq(DictKeyDO::getId, request.getId())
                .like(DictKeyDO::getKeyName, request.getKeyName())
                .like(DictKeyDO::getDescription, request.getDescription())
                .and(Strings.isNotEmpty(searchValue), c -> c
                        .like(DictKeyDO::getKeyName, searchValue).or()
                        .like(DictKeyDO::getDescription, searchValue)
                ).orderByDesc(DictKeyDO::getId);
    }

}
