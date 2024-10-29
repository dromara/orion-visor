/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.constant.ErrorMessage;
import org.dromara.visor.framework.common.security.PasswordModifier;
import org.dromara.visor.framework.common.utils.CryptoUtils;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.RedisUtils;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.asset.convert.HostKeyConvert;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.dao.HostKeyDAO;
import org.dromara.visor.module.asset.define.cache.HostCacheKeyDefine;
import org.dromara.visor.module.asset.entity.domain.HostKeyDO;
import org.dromara.visor.module.asset.entity.dto.HostKeyCacheDTO;
import org.dromara.visor.module.asset.entity.request.host.HostKeyCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.HostKeyQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostKeyUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostKeyVO;
import org.dromara.visor.module.asset.service.HostKeyService;
import org.dromara.visor.module.infra.api.DataExtraApi;
import org.dromara.visor.module.infra.api.DataPermissionApi;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 主机密钥 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Slf4j
@Service
public class HostKeyServiceImpl implements HostKeyService {

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Resource
    private HostDAO hostDAO;

    @Resource
    private DataExtraApi dataExtraApi;

    @Resource
    private DataPermissionApi dataPermissionApi;

    @Override
    public Long createHostKey(HostKeyCreateRequest request) {
        log.info("HostKeyService-createHostKey request: {}", JSON.toJSONString(request));
        // 转换
        HostKeyDO record = HostKeyConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostKeyPresent(record);
        // 加密
        this.encryptKey(record);
        String password = record.getPassword();
        if (!Strings.isBlank(password)) {
            record.setPassword(CryptoUtils.encryptAsString(password));
        }
        // 插入
        int effect = hostKeyDAO.insert(record);
        log.info("HostKeyService-createHostKey effect: {}", effect);
        Long id = record.getId();
        // 删除缓存
        RedisMaps.delete(HostCacheKeyDefine.HOST_KEY);
        return id;
    }

    @Override
    public Integer updateHostKeyById(HostKeyUpdateRequest request) {
        log.info("HostKeyService-updateHostKeyById request: {}", JSON.toJSONString(request));
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        HostKeyDO record = hostKeyDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        HostKeyDO updateRecord = HostKeyConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostKeyPresent(updateRecord);
        // 加密
        this.encryptKey(updateRecord);
        // 设置密码
        String newPassword = PasswordModifier.getEncryptNewPassword(request);
        updateRecord.setPassword(newPassword);
        // 更新
        int effect = hostKeyDAO.updateById(updateRecord);
        // 删除缓存
        if (!record.getName().equals(updateRecord.getName())) {
            RedisMaps.delete(HostCacheKeyDefine.HOST_KEY);
        }
        log.info("HostKeyService-updateHostKeyById effect: {}", effect);
        return effect;
    }

    @Override
    public HostKeyVO getHostKeyById(Long id) {
        // 查询
        HostKeyDO record = hostKeyDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 解密密钥
        this.decryptKey(record);
        // 转换
        return HostKeyConvert.MAPPER.to(record);
    }

    @Override
    public HostKeyDO getHostKey(Long id) {
        HostKeyDO record = hostKeyDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 解密密钥
        this.decryptKey(record);
        // 解密密码
        String password = record.getPassword();
        if (!Strings.isBlank(password)) {
            record.setPassword(CryptoUtils.decryptAsString(password));
        }
        return record;
    }

    @Override
    public List<HostKeyVO> getHostKeyList() {
        // 查询缓存
        List<HostKeyCacheDTO> list = RedisMaps.valuesJson(HostCacheKeyDefine.HOST_KEY);
        if (list.isEmpty()) {
            // 查询数据库
            list = hostKeyDAO.of().list(HostKeyConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, HostKeyCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(HostCacheKeyDefine.HOST_KEY, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(HostKeyConvert.MAPPER::to)
                .sorted(Comparator.comparing(HostKeyVO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public DataGrid<HostKeyVO> getHostKeyPage(HostKeyQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostKeyDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        DataGrid<HostKeyVO> dataGrid = hostKeyDAO.of(wrapper)
                .page(request)
                .dataGrid(HostKeyConvert.MAPPER::to);
        dataGrid.forEach(this::toSafe);
        return dataGrid;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteHostKeyById(Long id) {
        return this.deleteHostKeyByIdList(Lists.singleton(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteHostKeyByIdList(List<Long> idList) {
        log.info("HostKeyService-deleteHostKeyById idList: {}", idList);
        // 检查数据是否存在
        List<HostKeyDO> list = hostKeyDAO.selectBatchIds(idList);
        Valid.notEmpty(list, ErrorMessage.DATA_ABSENT);
        // 添加日志参数
        String name = list.stream()
                .map(HostKeyDO::getName)
                .collect(Collectors.joining(Const.COMMA));
        OperatorLogs.add(OperatorLogs.NAME, name);
        // 删除数据库
        int effect = hostKeyDAO.deleteBatchIds(idList);
        // 删除关联
        hostIdentityDAO.setKeyWithNull(idList);
        // 删除主机配置
        hostDAO.setKeyIdWithNull(idList);
        // 删除主机密钥额外配置
        dataExtraApi.deleteHostKeyExtra(idList);
        // 删除数据权限
        dataPermissionApi.deleteByRelIdList(DataPermissionTypeEnum.HOST_KEY, idList);
        // 删除缓存
        RedisUtils.delete(HostCacheKeyDefine.HOST_KEY);
        log.info("HostKeyService-deleteHostKeyById effect: {}", effect);
        return effect;
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkHostKeyPresent(HostKeyDO domain) {
        // 构造条件
        LambdaQueryWrapper<HostKeyDO> wrapper = hostKeyDAO.wrapper()
                // 更新时忽略当前记录
                .ne(HostKeyDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(HostKeyDO::getName, domain.getName());
        // 检查是否存在
        boolean present = hostKeyDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<HostKeyDO> buildQueryWrapper(HostKeyQueryRequest request) {
        String searchValue = request.getSearchValue();
        return hostKeyDAO.wrapper()
                .eq(HostKeyDO::getId, request.getId())
                .like(HostKeyDO::getName, request.getName())
                .and(Strings.isNotEmpty(searchValue), c -> c
                        .eq(HostKeyDO::getId, searchValue).or()
                        .like(HostKeyDO::getName, searchValue)
                );
    }

    /**
     * 删除不安全字段
     *
     * @param vo vo
     */
    public void toSafe(HostKeyVO vo) {
        vo.setPublicKey(null);
        vo.setPrivateKey(null);
    }

    /**
     * 加密 公钥 私钥
     *
     * @param record record
     */
    private void encryptKey(HostKeyDO record) {
        String publicKey = record.getPublicKey();
        if (!Strings.isBlank(publicKey)) {
            record.setPublicKey(CryptoUtils.encryptAsString(publicKey));
        }
        String privateKey = record.getPrivateKey();
        if (!Strings.isBlank(privateKey)) {
            record.setPrivateKey(CryptoUtils.encryptAsString(privateKey));
        }
    }

    /**
     * 解密 公钥 私钥
     *
     * @param record record
     */
    private void decryptKey(HostKeyDO record) {
        String publicKey = record.getPublicKey();
        if (!Strings.isBlank(publicKey)) {
            record.setPublicKey(CryptoUtils.decryptAsString(publicKey));
        }
        String privateKey = record.getPrivateKey();
        if (!Strings.isBlank(privateKey)) {
            record.setPrivateKey(CryptoUtils.decryptAsString(privateKey));
        }
    }

}
