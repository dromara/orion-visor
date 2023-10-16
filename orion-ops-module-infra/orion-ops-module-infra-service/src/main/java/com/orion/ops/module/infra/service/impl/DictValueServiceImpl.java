package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.module.infra.convert.DictValueConvert;
import com.orion.ops.module.infra.dao.DictKeyDAO;
import com.orion.ops.module.infra.dao.DictValueDAO;
import com.orion.ops.module.infra.define.cache.DictCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.DictKeyDO;
import com.orion.ops.module.infra.entity.domain.DictValueDO;
import com.orion.ops.module.infra.entity.dto.DictValueCacheDTO;
import com.orion.ops.module.infra.entity.request.dict.DictValueCreateRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueQueryRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueUpdateRequest;
import com.orion.ops.module.infra.entity.vo.DictValueVO;
import com.orion.ops.module.infra.service.DictValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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

    @Override
    public Long createDictValue(DictValueCreateRequest request) {
        log.info("DictValueService-createDictValue request: {}", JSON.toJSONString(request));
        // 转换
        DictValueDO record = DictValueConvert.MAPPER.to(request);
        // 查询 key 是否存在
        DictKeyDO key = dictKeyDAO.selectById(request.getKeyId());
        Valid.notNull(key, ErrorMessage.CONFIG_ABSENT);
        String keyName = record.getKey();
        // 查询数据是否冲突
        this.checkDictValuePresent(record);
        // 插入
        int effect = dictValueDAO.insert(record);
        Long id = record.getId();
        log.info("DictValueService-createDictValue id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(DictCacheKeyDefine.DICT_VALUE);
        return id;
    }

    @Override
    public Integer updateDictValueById(DictValueUpdateRequest request) {
        log.info("DictValueService-updateDictValueById id: {}, request: {}", request.getId(), JSON.toJSONString(request));
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        DictValueDO record = dictValueDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        DictValueDO updateRecord = DictValueConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDictValuePresent(updateRecord);
        // 更新
        int effect = dictValueDAO.updateById(updateRecord);
        log.info("DictValueService-updateDictValueById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DictCacheKeyDefine.DICT_VALUE);
        return effect;
    }

    @Override
    public DictValueVO getDictValueById(Long id) {
        // 查询
        DictValueDO record = dictValueDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return DictValueConvert.MAPPER.to(record);
    }

    @Override
    public List<DictValueVO> getDictValueList(DictValueQueryRequest request) {
        // 条件
        LambdaQueryWrapper<DictValueDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return dictValueDAO.of(wrapper).list(DictValueConvert.MAPPER::to);
    }

    @Override
    public List<DictValueVO> getDictValueListByCache() {
        // 查询缓存
        List<DictValueCacheDTO> list = RedisMaps.valuesJson(DictCacheKeyDefine.DICT_VALUE);
        if (list.isEmpty()) {
            // 查询数据库
            list = dictValueDAO.of().list(DictValueConvert.MAPPER::toCache);
            // 添加默认值 防止穿透
            if (list.isEmpty()) {
                list.add(DictValueCacheDTO.builder()
                        .id(Const.NONE_ID)
                        .build());
            }
            // 设置缓存
            RedisMaps.putAllJson(DictCacheKeyDefine.DICT_VALUE.getKey(), s -> s.getId().toString(), list);
            RedisMaps.setExpire(DictCacheKeyDefine.DICT_VALUE);
        }
        // 删除默认值
        return list.stream()
                .filter(s -> !s.getId().equals(Const.NONE_ID))
                .map(DictValueConvert.MAPPER::to)
                .collect(Collectors.toList());
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
    public Integer deleteDictValueById(Long id) {
        log.info("DictValueService-deleteDictValueById id: {}", id);
        // 检查数据是否存在
        DictValueDO record = dictValueDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = dictValueDAO.deleteById(id);
        log.info("DictValueService-deleteDictValueById id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(DictCacheKeyDefine.DICT_VALUE, id);
        return effect;
    }

    @Override
    public Integer batchDeleteDictValueByIdList(List<Long> idList) {
        log.info("DictValueService-batchDeleteDictValueByIdList idList: {}", idList);
        int effect = dictValueDAO.deleteBatchIds(idList);
        log.info("DictValueService-batchDeleteDictValueByIdList effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DictCacheKeyDefine.DICT_VALUE, idList);
        return effect;
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
                .eq(DictValueDO::getLabel, domain.getLabel());
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
                .eq(DictValueDO::getLabel, request.getLabel())
                .eq(DictValueDO::getValue, request.getValue())
                .eq(DictValueDO::getDesc, request.getDesc());
    }

}
