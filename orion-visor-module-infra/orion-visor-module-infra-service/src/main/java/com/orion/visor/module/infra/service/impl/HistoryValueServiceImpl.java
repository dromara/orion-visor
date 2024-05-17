package com.orion.visor.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Strings;
import com.orion.visor.module.infra.convert.HistoryValueConvert;
import com.orion.visor.module.infra.dao.HistoryValueDAO;
import com.orion.visor.module.infra.entity.domain.HistoryValueDO;
import com.orion.visor.module.infra.entity.request.history.HistoryValueCreateRequest;
import com.orion.visor.module.infra.entity.request.history.HistoryValueQueryRequest;
import com.orion.visor.module.infra.entity.vo.HistoryValueVO;
import com.orion.visor.module.infra.service.HistoryValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 历史归档 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Slf4j
@Service
public class HistoryValueServiceImpl implements HistoryValueService {

    @Resource
    private HistoryValueDAO historyValueDAO;

    @Override
    public Long createHistoryValue(HistoryValueCreateRequest request) {
        log.info("HistoryValueService-createHistoryValue request: {}", JSON.toJSONString(request));
        // 转换
        HistoryValueDO record = HistoryValueConvert.MAPPER.to(request);
        // 插入
        int effect = historyValueDAO.insert(record);
        Long id = record.getId();
        log.info("HistoryValueService-createHistoryValue id: {}, effect: {}", id, effect);
        return id;
    }

    @Override
    public DataGrid<HistoryValueVO> getHistoryValuePage(HistoryValueQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HistoryValueDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return historyValueDAO.of(wrapper)
                .page(request)
                .dataGrid(HistoryValueConvert.MAPPER::to);
    }

    @Override
    public HistoryValueDO getHistoryById(Long id) {
        return historyValueDAO.selectById(id);
    }

    @Override
    public HistoryValueDO getHistoryByRelId(Long id, Long relId, String type) {
        return historyValueDAO.of()
                .createWrapper()
                .eq(HistoryValueDO::getId, id)
                .eq(HistoryValueDO::getRelId, relId)
                .eq(HistoryValueDO::getType, type)
                .then()
                .getOne();
    }

    @Override
    public Integer deleteByRelId(String type, Long relId) {
        log.info("HistoryValueService-deleteByRelId type: {}, relId: {}", type, relId);
        int effect = historyValueDAO.deleteByRelId(type, relId);
        log.info("HistoryValueService-deleteByRelId type: {}, effect: {}", type, effect);
        return effect;
    }

    @Override
    public Integer deleteByRelIdList(String type, List<Long> relIdList) {
        log.info("HistoryValueService-deleteByRelIdList type: {}, relIdList: {}", type, relIdList);
        int effect = historyValueDAO.deleteByRelIdList(type, relIdList);
        log.info("HistoryValueService-deleteByRelIdList type: {}, effect: {}", type, effect);
        return effect;
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<HistoryValueDO> buildQueryWrapper(HistoryValueQueryRequest request) {
        String searchValue = request.getSearchValue();
        return historyValueDAO.wrapper()
                .eq(HistoryValueDO::getRelId, request.getRelId())
                .eq(HistoryValueDO::getType, request.getType())
                .and(Strings.isNotEmpty(searchValue), c -> c
                        .like(HistoryValueDO::getBeforeValue, searchValue).or()
                        .like(HistoryValueDO::getAfterValue, searchValue)
                )
                .orderByDesc(HistoryValueDO::getId);
    }

}
