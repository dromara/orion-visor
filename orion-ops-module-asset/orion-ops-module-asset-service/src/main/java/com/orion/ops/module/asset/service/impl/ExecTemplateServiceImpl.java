package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.ExecTemplateConvert;
import com.orion.ops.module.asset.dao.ExecTemplateDAO;
import com.orion.ops.module.asset.entity.domain.ExecTemplateDO;
import com.orion.ops.module.asset.entity.request.exec.ExecTemplateCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecTemplateQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecTemplateUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecTemplateVO;
import com.orion.ops.module.asset.enums.HostConfigTypeEnum;
import com.orion.ops.module.asset.enums.ScriptExecEnum;
import com.orion.ops.module.asset.service.AssetAuthorizedDataService;
import com.orion.ops.module.asset.service.ExecTemplateHostService;
import com.orion.ops.module.asset.service.ExecTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

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

    @Resource
    private ExecTemplateHostService execTemplateHostService;

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @Override
    public Long createExecTemplate(ExecTemplateCreateRequest request) {
        log.info("ExecTemplateService-createExecTemplate request: {}", JSON.toJSONString(request));
        Valid.valid(ScriptExecEnum::of, request.getScriptExec());
        // 转换
        ExecTemplateDO record = ExecTemplateConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkExecTemplatePresent(record);
        // 插入模板
        int effect = execTemplateDAO.insert(record);
        Long id = record.getId();
        // 修改模板主机
        execTemplateHostService.setTemplateHost(id, request.getHostIdList());
        log.info("ExecTemplateService-createExecTemplate id: {}, effect: {}", id, effect);
        return id;
    }

    @Override
    public Integer updateExecTemplateById(ExecTemplateUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        Valid.valid(ScriptExecEnum::of, request.getScriptExec());
        log.info("ExecTemplateService-updateExecTemplateById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        ExecTemplateDO record = execTemplateDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        ExecTemplateDO updateRecord = ExecTemplateConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkExecTemplatePresent(updateRecord);
        // 更新模板
        int effect = execTemplateDAO.updateById(updateRecord);
        // 修改模板主机
        execTemplateHostService.setTemplateHost(id, request.getHostIdList());
        log.info("ExecTemplateService-updateExecTemplateById effect: {}", effect);
        return effect;
    }

    @Override
    public ExecTemplateVO getExecTemplateById(Long id) {
        // 查询模板
        ExecTemplateDO record = execTemplateDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        ExecTemplateVO template = ExecTemplateConvert.MAPPER.to(record);
        // 查询模板主机
        Set<Long> hosts = execTemplateHostService.getHostByTemplateId(id);
        template.setHostIdList(hosts);
        return template;
    }

    @Override
    public ExecTemplateVO getExecTemplateWithAuthorized(Long id) {
        // 查询模板
        ExecTemplateDO record = execTemplateDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        ExecTemplateVO template = ExecTemplateConvert.MAPPER.to(record);
        // 查询模板主机
        Set<Long> hostIdList = execTemplateHostService.getHostByTemplateId(id);
        if (Lists.isEmpty(hostIdList)) {
            return template;
        }
        // 过滤认证的主机
        List<Long> authorizedHostIdList = assetAuthorizedDataService.getUserAuthorizedHostIdWithEnabledConfig(SecurityUtils.getLoginUserId(), HostConfigTypeEnum.SSH);
        hostIdList.removeIf(s -> !authorizedHostIdList.contains(s));
        template.setHostIdList(hostIdList);
        return template;
    }

    @Override
    public DataGrid<ExecTemplateVO> getExecTemplatePage(ExecTemplateQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecTemplateDO> wrapper = this.buildQueryWrapper(request);
        // 查询模板
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
        // 删除模板
        int effect = execTemplateDAO.deleteById(id);
        log.info("ExecTemplateService-deleteExecTemplateById id: {}, effect: {}", id, effect);
        // 删除模板主机
        effect += execTemplateHostService.deleteByTemplateId(id);
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
                .like(ExecTemplateDO::getCommand, request.getCommand())
                .orderByDesc(ExecTemplateDO::getId);
    }

}
