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
package org.dromara.visor.module.asset.service.impl;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.security.PasswordModifier;
import org.dromara.visor.common.utils.AesEncryptUtils;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.RedisUtils;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.asset.convert.HostIdentityConvert;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.dao.HostKeyDAO;
import org.dromara.visor.module.asset.define.cache.HostCacheKeyDefine;
import org.dromara.visor.module.asset.entity.domain.HostIdentityDO;
import org.dromara.visor.module.asset.entity.domain.HostKeyDO;
import org.dromara.visor.module.asset.entity.dto.HostIdentityCacheDTO;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostIdentityVO;
import org.dromara.visor.module.asset.enums.HostIdentityTypeEnum;
import org.dromara.visor.module.asset.service.HostIdentityService;
import org.dromara.visor.module.infra.api.DataExtraApi;
import org.dromara.visor.module.infra.api.DataPermissionApi;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 主机身份 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Slf4j
@Service
public class HostIdentityServiceImpl implements HostIdentityService {

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private HostDAO hostDAO;

    @Resource
    private DataExtraApi dataExtraApi;

    @Resource
    private DataPermissionApi dataPermissionApi;

    @Override
    public Long createHostIdentity(HostIdentityCreateRequest request) {
        log.info("HostIdentityService-createHostIdentity request: {}", JSON.toJSONString(request));
        // 检查密钥是否存在
        this.checkCreateParams(request);
        // 转换
        HostIdentityDO record = HostIdentityConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostIdentityPresent(record);
        // 加密密码
        String password = record.getPassword();
        if (!Strings.isBlank(password)) {
            record.setPassword(AesEncryptUtils.encryptAsString(password));
        }
        // 插入
        int effect = hostIdentityDAO.insert(record);
        log.info("HostIdentityService-createHostIdentity effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(HostCacheKeyDefine.HOST_IDENTITY);
        return record.getId();
    }

    @Override
    public Integer updateHostIdentityById(HostIdentityUpdateRequest request) {
        log.info("HostIdentityService-updateHostIdentityById request: {}", JSON.toJSONString(request));
        // 验证参数
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        HostIdentityTypeEnum type = Valid.valid(HostIdentityTypeEnum::of, request.getType());
        if (HostIdentityTypeEnum.KEY.equals(type)) {
            // 密钥认证
            this.checkKeyId(request.getKeyId());
        }
        // 查询主机身份
        HostIdentityDO record = hostIdentityDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        HostIdentityDO updateRecord = HostIdentityConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostIdentityPresent(updateRecord);
        if (HostIdentityTypeEnum.PASSWORD.equals(type)) {
            // 设置密码
            String newPassword = PasswordModifier.getEncryptNewPassword(request);
            updateRecord.setPassword(newPassword);
        }
        // 更新
        LambdaUpdateWrapper<HostIdentityDO> wrapper = Wrappers.<HostIdentityDO>lambdaUpdate()
                .set(HostIdentityDO::getKeyId, request.getKeyId())
                .eq(HostIdentityDO::getId, id);
        int effect = hostIdentityDAO.update(updateRecord, wrapper);
        log.info("HostIdentityService-updateHostIdentityById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(HostCacheKeyDefine.HOST_IDENTITY);
        return effect;
    }

    @Override
    public HostIdentityVO getHostIdentityById(Long id) {
        // 查询
        HostIdentityDO record = hostIdentityDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return HostIdentityConvert.MAPPER.to(record);
    }

    @Override
    public List<HostIdentityVO> getHostIdentityList() {
        // 查询缓存
        List<HostIdentityCacheDTO> list = RedisMaps.valuesJson(HostCacheKeyDefine.HOST_IDENTITY);
        if (list.isEmpty()) {
            // 查询数据库
            list = hostIdentityDAO.of().list(HostIdentityConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, HostIdentityCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(HostCacheKeyDefine.HOST_IDENTITY, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(HostIdentityConvert.MAPPER::to)
                .sorted(Comparator.comparing(HostIdentityVO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public DataGrid<HostIdentityVO> getHostIdentityPage(HostIdentityQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostIdentityDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        DataGrid<HostIdentityVO> dataGrid = hostIdentityDAO.of(wrapper)
                .page(request)
                .dataGrid(HostIdentityConvert.MAPPER::to);
        if (dataGrid.isEmpty()) {
            return dataGrid;
        }
        // 设置密钥名称
        List<Long> keyIdList = dataGrid.stream()
                .filter(s -> HostIdentityTypeEnum.KEY.name().equals(s.getType()))
                .map(HostIdentityVO::getKeyId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (!keyIdList.isEmpty()) {
            // 查询密钥名称
            Map<Long, String> keyNameMap = hostKeyDAO.selectBatchIds(keyIdList)
                    .stream()
                    .collect(Collectors.toMap(HostKeyDO::getId, HostKeyDO::getName));
            dataGrid.forEach(s -> {
                if (s.getKeyId() == null) {
                    return;
                }
                s.setKeyName(keyNameMap.get(s.getKeyId()));
            });
        }
        return dataGrid;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteHostIdentityById(Long id) {
        return this.deleteHostIdentityByIdList(Lists.singleton(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteHostIdentityByIdList(List<Long> idList) {
        log.info("HostIdentityService-deleteHostIdentityByIdList idList: {}", idList);
        // 检查数据是否存在
        List<HostIdentityDO> list = hostIdentityDAO.selectBatchIds(idList);
        Valid.notEmpty(list, ErrorMessage.DATA_ABSENT);
        // 添加日志参数
        String name = list.stream()
                .map(HostIdentityDO::getName)
                .collect(Collectors.joining(Const.COMMA));
        OperatorLogs.add(OperatorLogs.NAME, name);
        // 删除数据库
        int effect = hostIdentityDAO.deleteBatchIds(idList);
        // 删除主机配置
        hostDAO.setIdentityIdWithNull(idList);
        // 删除主机身份额外配置
        dataExtraApi.deleteHostIdentityExtra(idList);
        // 删除数据权限
        dataPermissionApi.deleteByRelIdList(DataPermissionTypeEnum.HOST_IDENTITY, idList);
        // 删除缓存
        RedisUtils.delete(HostCacheKeyDefine.HOST_IDENTITY);
        log.info("HostIdentityService-deleteHostIdentityByIdList effect: {}", effect);
        return effect;
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkHostIdentityPresent(HostIdentityDO domain) {
        // 构造条件
        LambdaQueryWrapper<HostIdentityDO> wrapper = hostIdentityDAO.wrapper()
                // 更新时忽略当前记录
                .ne(HostIdentityDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(HostIdentityDO::getName, domain.getName());
        // 检查是否存在
        boolean present = hostIdentityDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 检查创建参数
     *
     * @param request request
     */
    private void checkCreateParams(HostIdentityCreateRequest request) {
        HostIdentityTypeEnum type = Valid.valid(HostIdentityTypeEnum::of, request.getType());
        if (HostIdentityTypeEnum.PASSWORD.equals(type)) {
            // 密码认证
            Valid.notBlank(request.getPassword(), ErrorMessage.PARAM_MISSING);
        } else if (HostIdentityTypeEnum.KEY.equals(type)) {
            // 密钥认证
            this.checkKeyId(request.getKeyId());
        }
    }

    /**
     * 检查 keyId 是否存在
     *
     * @param keyId keyId
     */
    private void checkKeyId(Long keyId) {
        Valid.notNull(keyId, ErrorMessage.PARAM_MISSING);
        Valid.notNull(hostKeyDAO.selectById(keyId), ErrorMessage.KEY_ABSENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<HostIdentityDO> buildQueryWrapper(HostIdentityQueryRequest request) {
        String searchValue = request.getSearchValue();
        return hostIdentityDAO.wrapper()
                .eq(HostIdentityDO::getId, request.getId())
                .eq(HostIdentityDO::getType, request.getType())
                .eq(HostIdentityDO::getKeyId, request.getKeyId())
                .like(HostIdentityDO::getName, request.getName())
                .like(HostIdentityDO::getUsername, request.getUsername())
                .like(HostIdentityDO::getDescription, request.getDescription())
                .and(Strings.isNotEmpty(searchValue), c -> c
                        .eq(HostIdentityDO::getId, searchValue).or()
                        .like(HostIdentityDO::getName, searchValue).or()
                        .like(HostIdentityDO::getUsername, searchValue)
                );
    }

}
