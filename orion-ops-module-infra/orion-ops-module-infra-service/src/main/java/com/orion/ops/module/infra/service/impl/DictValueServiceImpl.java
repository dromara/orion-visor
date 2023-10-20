package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.mybatis.core.query.Conditions;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.module.infra.convert.DictValueConvert;
import com.orion.ops.module.infra.dao.DictKeyDAO;
import com.orion.ops.module.infra.dao.DictValueDAO;
import com.orion.ops.module.infra.define.cache.DictCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.DictKeyDO;
import com.orion.ops.module.infra.entity.domain.DictValueDO;
import com.orion.ops.module.infra.entity.domain.HistoryValueDO;
import com.orion.ops.module.infra.entity.dto.DictValueCacheDTO;
import com.orion.ops.module.infra.entity.request.dict.DictValueCreateRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueQueryRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueRollbackRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueUpdateRequest;
import com.orion.ops.module.infra.entity.request.history.HistoryValueCreateRequest;
import com.orion.ops.module.infra.entity.vo.DictValueVO;
import com.orion.ops.module.infra.enums.DictValueTypeEnum;
import com.orion.ops.module.infra.enums.HistoryValueTypeEnum;
import com.orion.ops.module.infra.service.DictKeyService;
import com.orion.ops.module.infra.service.DictValueService;
import com.orion.ops.module.infra.service.HistoryValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典配置值 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Slf4j
@Service
public class DictValueServiceImpl implements DictValueService {

    @Resource
    private DictKeyDAO dictKeyDAO;

    @Resource
    private DictValueDAO dictValueDAO;

    @Resource
    private DictKeyService dictKeyService;

    @Resource
    private HistoryValueService historyValueService;

    @Override
    public Long createDictValue(DictValueCreateRequest request) {
        log.info("DictValueService-createDictValue request: {}", JSON.toJSONString(request));
        // 转换
        DictValueDO record = DictValueConvert.MAPPER.to(request);
        // 查询 dictKey 是否存在
        DictKeyDO dictKey = dictKeyDAO.selectById(request.getKeyId());
        String key = Valid.notNull(dictKey, ErrorMessage.CONFIG_ABSENT).getKeyName();
        // 查询数据是否冲突
        this.checkDictValuePresent(record);
        // 插入
        OperatorLogs.add(OperatorLogs.KEY_NAME, dictKey);
        record.setKeyName(key);
        int effect = dictValueDAO.insert(record);
        Long id = record.getId();
        log.info("DictValueService-createDictValue id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(DictCacheKeyDefine.DICT_VALUE.format(key));
        return id;
    }

    @Override
    public Integer updateDictValueById(DictValueUpdateRequest request) {
        log.info("DictValueService-updateDictValueById id: {}, request: {}", request.getId(), JSON.toJSONString(request));
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        DictValueDO record = dictValueDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.CONFIG_ABSENT);
        // 查询 dictKey 是否存在
        DictKeyDO dictKey = dictKeyDAO.selectById(request.getKeyId());
        String key = Valid.notNull(dictKey, ErrorMessage.CONFIG_ABSENT).getKeyName();
        // 转换
        DictValueDO updateRecord = DictValueConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDictValuePresent(updateRecord);
        // 更新
        OperatorLogs.add(OperatorLogs.KEY_NAME, dictKey);
        updateRecord.setKeyName(key);
        int effect = dictValueDAO.updateById(updateRecord);
        log.info("DictValueService-updateDictValueById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DictCacheKeyDefine.DICT_VALUE.format(key));
        // 记录历史归档
        this.checkRecordHistory(updateRecord, record);
        return effect;
    }

    @Override
    public Integer rollbackDictValueById(DictValueRollbackRequest request) {
        Long id = request.getId();
        log.info("DictValueService-updateDictValueById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        DictValueDO record = dictValueDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.CONFIG_ABSENT);
        // 查询历史值
        HistoryValueDO history = historyValueService.getHistoryByRelId(request.getValueId(), id, HistoryValueTypeEnum.DICT.name());
        Valid.notNull(history, ErrorMessage.HISTORY_ABSENT);
        // 记录日志参数
        OperatorLogs.add(OperatorLogs.KEY_NAME, record.getKeyName());
        OperatorLogs.add(OperatorLogs.LABEL, record.getName());
        OperatorLogs.add(OperatorLogs.VALUE, history.getBeforeValue());
        // 更新
        DictValueDO updateRecord = new DictValueDO();
        updateRecord.setId(id);
        updateRecord.setValue(history.getBeforeValue());
        int effect = dictValueDAO.updateById(updateRecord);
        log.info("DictValueService-rollbackDictValueById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DictCacheKeyDefine.DICT_VALUE.format(record.getKeyName()));
        // 记录历史归档
        this.checkRecordHistory(updateRecord, record);
        return effect;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DictValueVO> getDictValueList(String keyName) {
        // 查询缓存
        String cacheKey = DictCacheKeyDefine.DICT_VALUE.format(keyName);
        List<DictValueCacheDTO> list = RedisMaps.valuesJson(cacheKey, (Class<DictValueCacheDTO>) DictCacheKeyDefine.DICT_VALUE.getType());
        if (list.isEmpty()) {
            // 查询数据库
            list = dictValueDAO.of()
                    .createWrapper()
                    .eq(DictValueDO::getKeyName, keyName)
                    .then()
                    .list(DictValueConvert.MAPPER::toCache);
            // 添加默认值 防止穿透
            if (list.isEmpty()) {
                list.add(DictValueCacheDTO.builder()
                        .id(Const.NONE_ID)
                        .build());
            }
            // 设置缓存
            RedisMaps.putAllJson(cacheKey, s -> s.getId().toString(), list);
            RedisMaps.setExpire(cacheKey, DictCacheKeyDefine.DICT_VALUE);
        }
        // 删除默认值
        return list.stream()
                .filter(s -> !s.getId().equals(Const.NONE_ID))
                .map(DictValueConvert.MAPPER::to)
                .sorted(Comparator.comparing(DictValueVO::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Map<String, Object>> getDictValueEnum(String keyName) {
        // 查询配置值
        List<DictValueVO> values = this.getDictValueList(keyName);
        if (values.isEmpty()) {
            return Maps.empty();
        }
        // 查询配置项
        Map<String, String> schema = dictKeyService.getDictSchema(keyName);
        // 返回
        Map<String, Map<String, Object>> result = Maps.newLinkedMap();
        for (DictValueVO value : values) {
            Map<String, Object> item = Maps.newMap();
            item.put(Const.NAME, value.getName());
            item.put(Const.LABEL, value.getLabel());
            item.put(Const.VALUE, DictValueTypeEnum.of(schema.get(Const.VALUE)).parse(value.getValue()));
            // 额外值
            String extra = value.getExtra();
            if (!Strings.isBlank(extra)) {
                JSONObject extraObject = JSON.parseObject(extra);
                for (String extraKey : extraObject.keySet()) {
                    item.put(extraKey, DictValueTypeEnum.of(schema.get(extraKey)).parse(extraObject.getString(extraKey)));
                }
            }
            result.put(value.getName(), item);
        }
        return result;
    }

    @Override
    public DataGrid<DictValueVO> getDictValuePage(DictValueQueryRequest request) {
        // 条件
        LambdaQueryWrapper<DictValueDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return dictValueDAO.of(wrapper)
                .page(request)
                .dataGrid(DictValueConvert.MAPPER::to);
    }

    @Override
    public Integer updateKeyNameByKeyId(Long keyId, String beforeKey, String newKey) {
        // 修改数据库
        DictValueDO updateRecord = new DictValueDO();
        updateRecord.setKeyName(newKey);
        LambdaQueryWrapper<DictValueDO> wrapper = dictValueDAO.lambda()
                .eq(DictValueDO::getKeyId, keyId);
        int effect = dictValueDAO.update(updateRecord, wrapper);
        // 删除缓存
        String beforeCacheKey = DictCacheKeyDefine.DICT_VALUE.format(beforeKey);
        String newCacheKey = DictCacheKeyDefine.DICT_VALUE.format(newKey);
        RedisMaps.delete(beforeCacheKey, newCacheKey);
        return effect;
    }

    @Override
    public Integer deleteDictValueById(Long id) {
        log.info("DictValueService-deleteDictValueById id: {}", id);
        // 检查数据是否存在
        DictValueDO record = dictValueDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.CONFIG_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.VALUE, record.getKeyName() + "-" + record.getName());
        // 删除
        return this.deleteDictValue(Lists.singleton(id), Lists.singleton(record));
    }

    @Override
    public Integer deleteDictValueByIdList(List<Long> idList) {
        log.info("DictValueService-deleteDictValueByIdList idList: {}", idList);
        // 查询数据
        List<DictValueDO> records = dictValueDAO.selectBatchIds(idList);
        // 添加日志参数
        String value = records.stream()
                .map(s -> s.getKeyName() + "-" + s.getName())
                .collect(Collectors.joining(Const.COMMA));
        OperatorLogs.add(OperatorLogs.VALUE, value);
        // 删除
        return this.deleteDictValue(idList, records);
    }

    @Override
    public Integer deleteDictValueByKeyId(Long keyId) {
        log.info("DictValueService-deleteDictValueByKeyId keyId: {}", keyId);
        // 查询数据
        List<DictValueDO> records = dictValueDAO.selectList(Conditions.eq(DictValueDO::getKeyId, keyId));
        // 删除
        List<Long> idList = records.stream()
                .map(DictValueDO::getId)
                .collect(Collectors.toList());
        return this.deleteDictValue(idList, records);
    }

    @Override
    public Integer deleteDictValueByKeyIdList(List<Long> keyIdList) {
        log.info("DictValueService-deleteDictValueByKeyIdList keyIdList: {}", keyIdList);
        // 查询数据
        List<DictValueDO> records = dictValueDAO.selectList(Conditions.in(DictValueDO::getKeyId, keyIdList));
        // 删除
        List<Long> idList = records.stream()
                .map(DictValueDO::getId)
                .collect(Collectors.toList());
        return this.deleteDictValue(idList, records);
    }

    /**
     * 删除字典配置项
     *
     * @param idList  idList
     * @param records records
     * @return effect
     */
    private int deleteDictValue(List<Long> idList, List<DictValueDO> records) {
        if (records.isEmpty()) {
            return 0;
        }
        // 删除
        int effect = dictValueDAO.deleteBatchIds(idList);
        log.info("DictValueService-deleteDictValue effect: {}", effect);
        // 删除缓存
        List<String> keyList = records.stream()
                .map(DictValueDO::getKeyName)
                .distinct()
                .map(DictCacheKeyDefine.DICT_VALUE::format)
                .collect(Collectors.toList());
        RedisMaps.delete(keyList);
        return effect;
    }

    /**
     * 检查是否保存默认值
     *
     * @param updateRecord 修改后
     * @param record       修改前
     */
    private void checkRecordHistory(DictValueDO updateRecord, DictValueDO record) {
        // 检查值是否发生改变
        String beforeValue = record.getValue();
        String afterValue = updateRecord.getValue();
        if (!beforeValue.equals(afterValue)) {
            // 记录历史值
            HistoryValueCreateRequest historyRequest = new HistoryValueCreateRequest();
            historyRequest.setRelId(record.getId());
            historyRequest.setType(HistoryValueTypeEnum.DICT.name());
            historyRequest.setBeforeValue(beforeValue);
            historyRequest.setAfterValue(afterValue);
            historyValueService.createHistoryValue(historyRequest);
        }
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkDictValuePresent(DictValueDO domain) {
        // 构造条件
        LambdaQueryWrapper<DictValueDO> wrapper = dictValueDAO.wrapper()
                // 更新时忽略当前记录
                .ne(DictValueDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(DictValueDO::getKeyId, domain.getKeyId())
                .eq(DictValueDO::getName, domain.getName());
        // 检查是否存在
        boolean present = dictValueDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.CONFIG_PRESENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<DictValueDO> buildQueryWrapper(DictValueQueryRequest request) {
        return dictValueDAO.wrapper()
                .eq(DictValueDO::getKeyId, request.getKeyId())
                .like(DictValueDO::getKeyName, request.getKeyName())
                .like(DictValueDO::getName, request.getName())
                .eq(DictValueDO::getValue, request.getValue())
                .like(DictValueDO::getLabel, request.getLabel())
                .orderByDesc(DictValueDO::getId);
    }

}
