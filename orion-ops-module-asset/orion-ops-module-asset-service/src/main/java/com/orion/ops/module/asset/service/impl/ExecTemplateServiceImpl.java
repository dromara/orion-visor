package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.module.asset.convert.ExecTemplateConvert;
import com.orion.ops.module.asset.dao.ExecTemplateDAO;
import com.orion.ops.module.asset.define.cache.ExecTemplateCacheKeyDefine;
import com.orion.ops.module.asset.entity.domain.ExecTemplateDO;
import com.orion.ops.module.asset.entity.dto.ExecTemplateCacheDTO;
import com.orion.ops.module.asset.entity.request.exec.ExecTemplateCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecTemplateQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecTemplateUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecTemplateVO;
import com.orion.ops.module.asset.service.ExecTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 执行模板 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
@Slf4j
@Service
public class ExecTemplateServiceImpl implements ExecTemplateService {

    @Resource
    private ExecTemplateDAO execTemplateDAO;

    @Override
    public Long createExecTemplate(ExecTemplateCreateRequest request) {
        log.info("ExecTemplateService-createExecTemplate request: {}", JSON.toJSONString(request));
        // 转换
        ExecTemplateDO record = ExecTemplateConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkExecTemplatePresent(record);
        // 插入
        int effect = execTemplateDAO.insert(record);
        Long id = record.getId();
        log.info("ExecTemplateService-createExecTemplate id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(ExecTemplateCacheKeyDefine.EXEC_TEMPLATE);
        return id;
    }

    @Override
    public Integer updateExecTemplateById(ExecTemplateUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("ExecTemplateService-updateExecTemplateById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        ExecTemplateDO record = execTemplateDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        ExecTemplateDO updateRecord = ExecTemplateConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkExecTemplatePresent(updateRecord);
        // 更新
        int effect = execTemplateDAO.updateById(updateRecord);
        log.info("ExecTemplateService-updateExecTemplateById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(ExecTemplateCacheKeyDefine.EXEC_TEMPLATE);
        return effect;
    }

    @Override
    public ExecTemplateVO getExecTemplateById(Long id) {
        // 查询
        ExecTemplateDO record = execTemplateDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return ExecTemplateConvert.MAPPER.to(record);
    }

    @Override
    public List<ExecTemplateVO> getExecTemplateListByCache() {
        // 查询缓存
        List<ExecTemplateCacheDTO> list = RedisMaps.valuesJson(ExecTemplateCacheKeyDefine.EXEC_TEMPLATE);
        if (list.isEmpty()) {
            // 查询数据库
            list = execTemplateDAO.of().list(ExecTemplateConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, ExecTemplateCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(ExecTemplateCacheKeyDefine.EXEC_TEMPLATE, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(ExecTemplateConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public DataGrid<ExecTemplateVO> getExecTemplatePage(ExecTemplateQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecTemplateDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execTemplateDAO.of(wrapper)
                .page(request)
                .dataGrid(ExecTemplateConvert.MAPPER::to);
    }

    @Override
    public Integer deleteExecTemplateById(Long id) {
        log.info("ExecTemplateService-deleteExecTemplateById id: {}", id);
        // 检查数据是否存在
        ExecTemplateDO record = execTemplateDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = execTemplateDAO.deleteById(id);
        log.info("ExecTemplateService-deleteExecTemplateById id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(ExecTemplateCacheKeyDefine.EXEC_TEMPLATE, id);
        return effect;
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkExecTemplatePresent(ExecTemplateDO domain) {
        // 构造条件
        LambdaQueryWrapper<ExecTemplateDO> wrapper = execTemplateDAO.wrapper()
                // 更新时忽略当前记录
                .ne(ExecTemplateDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(ExecTemplateDO::getName, domain.getName());
        // 检查是否存在
        boolean present = execTemplateDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<ExecTemplateDO> buildQueryWrapper(ExecTemplateQueryRequest request) {
        return execTemplateDAO.wrapper()
                .eq(ExecTemplateDO::getId, request.getId())
                .like(ExecTemplateDO::getName, request.getName())
                .like(ExecTemplateDO::getCommand, request.getCommand());
    }

}
