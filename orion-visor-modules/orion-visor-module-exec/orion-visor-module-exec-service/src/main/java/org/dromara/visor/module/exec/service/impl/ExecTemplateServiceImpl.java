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
package org.dromara.visor.module.exec.service.impl;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.collect.Lists;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.api.AssetAuthorizedDataApi;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.dromara.visor.module.exec.convert.ExecTemplateConvert;
import org.dromara.visor.module.exec.dao.ExecTemplateDAO;
import org.dromara.visor.module.exec.entity.domain.ExecTemplateDO;
import org.dromara.visor.module.exec.entity.request.exec.ExecTemplateCreateRequest;
import org.dromara.visor.module.exec.entity.request.exec.ExecTemplateQueryRequest;
import org.dromara.visor.module.exec.entity.request.exec.ExecTemplateUpdateRequest;
import org.dromara.visor.module.exec.entity.vo.ExecTemplateVO;
import org.dromara.visor.module.exec.service.ExecTemplateHostService;
import org.dromara.visor.module.exec.service.ExecTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
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

    @Resource
    private ExecTemplateHostService execTemplateHostService;

    @Resource
    private AssetAuthorizedDataApi assetAuthorizedDataApi;

    @Override
    public Long createExecTemplate(ExecTemplateCreateRequest request) {
        log.info("ExecTemplateService-createExecTemplate request: {}", JSON.toJSONString(request));
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
        List<Long> authorizedHostIdList = assetAuthorizedDataApi.getUserAuthorizedEnabledHostId(SecurityUtils.getLoginUserId(), HostTypeEnum.SSH);
        hostIdList.removeIf(s -> !authorizedHostIdList.contains(s));
        template.setHostIdList(hostIdList);
        return template;
    }

    @Override
    public DataGrid<ExecTemplateVO> getExecTemplatePage(ExecTemplateQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecTemplateDO> wrapper = this.buildQueryWrapper(request);
        // 查询模板
        return execTemplateDAO.of()
                .wrapper(wrapper)
                .page(request)
                .order(request, ExecTemplateDO::getId)
                .dataGrid(ExecTemplateConvert.MAPPER::to);
    }

    @Override
    public Long getExecTemplateCount(ExecTemplateQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecTemplateDO> wrapper = this.buildQueryWrapper(request);
        // 查询模板
        return execTemplateDAO.of()
                .wrapper(wrapper)
                .countMax(request.getLimit());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExecTemplateById(Long id) {
        return this.deleteExecTemplateByIdList(Lists.singleton(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExecTemplateByIdList(List<Long> idList) {
        log.info("ExecTemplateService-deleteExecTemplateByIdList idList: {}", idList);
        // 检查数据是否存在
        List<ExecTemplateDO> recordList = execTemplateDAO.selectBatchIds(idList);
        Valid.notEmpty(recordList, ErrorMessage.DATA_ABSENT);
        // 设置日志参数
        String name = recordList.stream()
                .map(ExecTemplateDO::getName)
                .collect(Collectors.joining(Const.COMMA));
        OperatorLogs.add(OperatorLogs.NAME, name);
        // 删除模板
        int effect = execTemplateDAO.deleteBatchIds(idList);
        log.info("ExecTemplateService-deleteExecTemplateByIdList idList: {}, effect: {}", idList, effect);
        // 删除模板主机
        effect += execTemplateHostService.deleteByTemplateIdList(idList);
        return effect;
    }

    @Override
    public LambdaQueryWrapper<ExecTemplateDO> buildQueryWrapper(ExecTemplateQueryRequest request) {
        return execTemplateDAO.wrapper()
                .eq(ExecTemplateDO::getId, request.getId())
                .like(ExecTemplateDO::getName, request.getName())
                .like(ExecTemplateDO::getCommand, request.getCommand());
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

}
