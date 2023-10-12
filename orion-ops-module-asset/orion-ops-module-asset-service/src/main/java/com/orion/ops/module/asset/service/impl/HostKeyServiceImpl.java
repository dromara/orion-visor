package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Strings;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.security.PasswordModifier;
import com.orion.ops.framework.common.utils.CryptoUtils;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.module.asset.convert.HostKeyConvert;
import com.orion.ops.module.asset.dao.HostConfigDAO;
import com.orion.ops.module.asset.dao.HostIdentityDAO;
import com.orion.ops.module.asset.dao.HostKeyDAO;
import com.orion.ops.module.asset.define.cache.HostCacheKeyDefine;
import com.orion.ops.module.asset.entity.domain.HostKeyDO;
import com.orion.ops.module.asset.entity.dto.HostKeyCacheDTO;
import com.orion.ops.module.asset.entity.request.host.HostKeyCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostKeyQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostKeyUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostKeyVO;
import com.orion.ops.module.asset.service.HostKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 主机秘钥 服务实现类
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
    private HostConfigDAO hostConfigDAO;

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
        // 解密秘钥
        this.decryptKey(record);
        // 转换
        return HostKeyConvert.MAPPER.to(record);
    }

    @Override
    public HostKeyDO getHostKey(Long id) {
        HostKeyDO record = hostKeyDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 解密秘钥
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
            // 添加默认值 防止穿透
            if (list.isEmpty()) {
                list.add(HostKeyCacheDTO.builder()
                        .id(Const.NONE_ID)
                        .build());
            }
            // 设置缓存
            RedisMaps.putAllJson(HostCacheKeyDefine.HOST_KEY.getKey(), s -> s.getId().toString(), list);
            RedisMaps.setExpire(HostCacheKeyDefine.HOST_KEY);
        }
        // 删除默认值
        return list.stream()
                .filter(s -> !s.getId().equals(Const.NONE_ID))
                .map(HostKeyConvert.MAPPER::to)
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
        log.info("HostKeyService-deleteHostKeyById id: {}", id);
        // 检查数据是否存在
        HostKeyDO record = hostKeyDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.NAME, record.getName());
        // 删除数据库
        int effect = hostKeyDAO.deleteById(id);
        // 删除关联
        hostIdentityDAO.setKeyWithNull(id);
        // 删除主机配置
        hostConfigDAO.setKeyIdWithNull(id);
        // 删除缓存
        RedisMaps.delete(HostCacheKeyDefine.HOST_KEY.getKey(), record.getId());
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
