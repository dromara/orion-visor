package com.orion.ops.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.function.Functions;
import com.orion.lang.utils.Refs;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.module.infra.dao.DataExtraDAO;
import com.orion.ops.module.infra.entity.domain.DataExtraDO;
import com.orion.ops.module.infra.entity.request.data.DataExtraQueryRequest;
import com.orion.ops.module.infra.entity.request.data.DataExtraUpdateRequest;
import com.orion.ops.module.infra.service.DataExtraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据拓展信息 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Slf4j
@Service
public class DataExtraServiceImpl implements DataExtraService {

    @Resource
    private DataExtraDAO dataExtraDAO;

    @Override
    public Integer updateExtraItem(DataExtraUpdateRequest request) {
        Long userId = request.getUserId();
        Long relId = request.getRelId();
        String type = request.getType();
        String item = request.getItem();
        Object value = request.getValue();
        // 查询配置是否存在
        DataExtraDO extraItem = dataExtraDAO.of()
                .createWrapper()
                .eq(DataExtraDO::getUserId, userId)
                .eq(DataExtraDO::getRelId, relId)
                .eq(DataExtraDO::getType, type)
                .eq(DataExtraDO::getItem, item)
                .then()
                .getOne();
        if (extraItem == null) {
            // 插入
            DataExtraDO insert = new DataExtraDO();
            insert.setUserId(userId);
            insert.setRelId(relId);
            insert.setType(type);
            insert.setItem(item);
            insert.setValue(Refs.json(value));
            return dataExtraDAO.insert(insert);
        } else {
            // 修改
            DataExtraDO update = new DataExtraDO();
            update.setId(extraItem.getId());
            update.setValue(Refs.json(value));
            return dataExtraDAO.updateById(update);
        }
    }

    @Override
    public void batchUpdate(Map<Long, Object> map) {
        if (Maps.isEmpty(map)) {
            return;
        }
        // 批量更新
        List<DataExtraDO> list = map.entrySet()
                .stream()
                .map(s -> {
                    DataExtraDO extra = new DataExtraDO();
                    extra.setId(s.getKey());
                    extra.setValue(Refs.json(s.getValue()));
                    return extra;
                }).collect(Collectors.toList());
        dataExtraDAO.updateBatch(list);
    }

    @Override
    public String getExtraItem(DataExtraQueryRequest request) {
        return dataExtraDAO.of()
                .wrapper(this.buildWrapper(request))
                .optionalOne()
                .map(DataExtraDO::getValue)
                .orElse(null);
    }

    @Override
    public Map<Long, String> getExtraItemList(DataExtraQueryRequest request) {
        return dataExtraDAO.of()
                .wrapper(this.buildWrapper(request))
                .stream()
                .collect(Collectors.toMap(DataExtraDO::getRelId,
                        DataExtraDO::getValue,
                        Functions.right())
                );
    }

    @Override
    public List<DataExtraDO> getExtraList(DataExtraQueryRequest request) {
        return dataExtraDAO.of()
                .wrapper(this.buildWrapper(request))
                .list();
    }

    @Override
    public Integer deleteByUserId(Long userId) {
        return dataExtraDAO.deleteByUserId(userId);
    }

    @Override
    public Integer deleteByRelId(String type, Long relId) {
        return dataExtraDAO.deleteByRelId(type, relId);
    }

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    private LambdaQueryWrapper<DataExtraDO> buildWrapper(DataExtraQueryRequest entity) {
        return dataExtraDAO.wrapper()
                .eq(DataExtraDO::getUserId, entity.getUserId())
                .eq(DataExtraDO::getRelId, entity.getRelId())
                .in(DataExtraDO::getRelId, entity.getRelIdList())
                .eq(DataExtraDO::getType, entity.getType())
                .eq(DataExtraDO::getItem, entity.getItem())
                .in(DataExtraDO::getItem, entity.getItems());
    }

}
