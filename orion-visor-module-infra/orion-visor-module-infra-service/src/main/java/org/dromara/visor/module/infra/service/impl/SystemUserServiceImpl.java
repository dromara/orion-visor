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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.crypto.Signatures;
import cn.orionsec.kit.spring.SpringHolder;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.constant.ErrorCode;
import org.dromara.visor.framework.common.constant.ErrorMessage;
import org.dromara.visor.framework.common.security.LoginUser;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.redis.core.utils.RedisUtils;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.api.CommandSnippetApi;
import org.dromara.visor.module.asset.api.PathBookmarkApi;
import org.dromara.visor.module.infra.convert.SystemRoleConvert;
import org.dromara.visor.module.infra.convert.SystemUserConvert;
import org.dromara.visor.module.infra.dao.OperatorLogDAO;
import org.dromara.visor.module.infra.dao.SystemRoleDAO;
import org.dromara.visor.module.infra.dao.SystemUserDAO;
import org.dromara.visor.module.infra.dao.SystemUserRoleDAO;
import org.dromara.visor.module.infra.define.cache.TipsCacheKeyDefine;
import org.dromara.visor.module.infra.define.cache.UserCacheKeyDefine;
import org.dromara.visor.module.infra.define.config.AppAuthenticationConfig;
import org.dromara.visor.module.infra.entity.domain.SystemRoleDO;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.dto.UserInfoDTO;
import org.dromara.visor.module.infra.entity.request.user.*;
import org.dromara.visor.module.infra.entity.vo.SystemUserVO;
import org.dromara.visor.module.infra.enums.UserStatusEnum;
import org.dromara.visor.module.infra.service.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:46
 */
@Slf4j
@Service
public class SystemUserServiceImpl implements SystemUserService {

    @Resource
    private AppAuthenticationConfig appAuthenticationConfig;

    @Resource
    private SystemUserDAO systemUserDAO;

    @Resource
    private SystemUserRoleDAO systemUserRoleDAO;

    @Resource
    private SystemRoleDAO systemRoleDAO;

    @Resource
    private OperatorLogDAO operatorLogDAO;

    @Resource
    private FavoriteService favoriteService;

    @Resource
    private PreferenceService preferenceService;

    @Resource
    private DataPermissionService dataPermissionService;

    @Resource
    private DataExtraService dataExtraService;

    @Resource
    private DataGroupService dataGroupService;

    @Resource
    private CommandSnippetApi commandSnippetApi;

    @Resource
    private PathBookmarkApi pathBookmarkApi;

    @Override
    public Long createSystemUser(SystemUserCreateRequest request) {
        // 转换
        SystemUserDO record = SystemUserConvert.MAPPER.to(request);
        // 查询用户名是否存在
        this.checkUsernamePresent(record);
        // 查询花名是否存在
        this.checkNicknamePresent(record);
        // 加密密码
        record.setPassword(Signatures.md5(request.getPassword()));
        // 插入
        int effect = systemUserDAO.insert(record);
        log.info("SystemUserService-createSystemUser effect: {}, record: {}", effect, JSON.toJSONString(record));
        // 删除用户缓存
        RedisUtils.delete(
                // 用户列表
                UserCacheKeyDefine.USER_LIST.getKey(),
                // 登录失败次数
                UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(request.getUsername())
        );
        return record.getId();
    }

    @Override
    public Integer updateSystemUserById(SystemUserUpdateRequest request) {
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        SystemUserDO record = systemUserDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.USER_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.USERNAME, record.getUsername());
        // 转换
        SystemUserDO updateRecord = SystemUserConvert.MAPPER.to(request);
        // 查询花名是否存在
        this.checkNicknamePresent(updateRecord);
        // 更新
        int effect = systemUserDAO.updateById(updateRecord);
        log.info("SystemUserService-updateSystemUserById effect: {}, updateRecord: {}", effect, JSON.toJSONString(updateRecord));
        // 更新缓存中的花名
        RedisStrings.<LoginUser>processSetJson(UserCacheKeyDefine.USER_INFO, s -> {
            s.setNickname(request.getNickname());
        }, id);
        // 删除用户列表缓存
        RedisUtils.delete(UserCacheKeyDefine.USER_LIST);
        return effect;
    }

    @Override
    public Integer updateUserStatus(SystemUserUpdateStatusRequest request) {
        Long id = request.getId();
        if (id.equals(SecurityUtils.getLoginUserId())) {
            throw ErrorCode.UNSUPPOETED.exception();
        }
        UserStatusEnum status = Valid.valid(UserStatusEnum::of, request.getStatus());
        // 查询用户
        SystemUserDO record = systemUserDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.USER_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.USERNAME, record.getUsername());
        OperatorLogs.add(OperatorLogs.STATUS_NAME, status.name());
        // 转换
        SystemUserDO updateRecord = SystemUserConvert.MAPPER.to(request);
        // 更新用户
        int effect = systemUserDAO.updateById(updateRecord);
        log.info("SystemUserService-updateUserStatus effect: {}, updateRecord: {}", effect, JSON.toJSONString(updateRecord));
        // 改为启用则删除登录失败次数缓存
        if (UserStatusEnum.ENABLED.equals(status)) {
            RedisUtils.delete(UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(record.getUsername()));
        }
        // 更新用户缓存中的 status
        RedisStrings.<LoginUser>processSetJson(UserCacheKeyDefine.USER_INFO, s -> {
            s.setStatus(request.getStatus());
        }, id);
        // 删除用户列表缓存
        RedisUtils.delete(UserCacheKeyDefine.USER_LIST);
        return effect;
    }

    @Override
    public SystemUserVO getSystemUserById(Long id) {
        // 查询用户
        SystemUserDO record = systemUserDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.USER_ABSENT);
        // 查询角色
        List<SystemRoleDO> roles = systemRoleDAO.selectRoleByUserId(id);
        // 返回
        SystemUserVO user = SystemUserConvert.MAPPER.to(record);
        user.setRoles(SystemRoleConvert.MAPPER.to(roles));
        return user;
    }

    @Override
    public List<SystemUserVO> getSystemUserList() {
        // 查询用户列表
        List<UserInfoDTO> list = RedisMaps.valuesJson(UserCacheKeyDefine.USER_LIST);
        if (list.isEmpty()) {
            // 查询数据库
            list = systemUserDAO.of().list(SystemUserConvert.MAPPER::toUserInfo);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, UserInfoDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(UserCacheKeyDefine.USER_LIST, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(SystemUserConvert.MAPPER::to)
                .sorted(Comparator.comparing(SystemUserVO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public DataGrid<SystemUserVO> getSystemUserPage(SystemUserQueryRequest request) {
        // 条件
        LambdaQueryWrapper<SystemUserDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return systemUserDAO.of()
                .page(request)
                .wrapper(wrapper)
                .dataGrid(SystemUserConvert.MAPPER::to);
    }

    @Override
    public Long getSystemUserCount(SystemUserQueryRequest request) {
        // 条件
        LambdaQueryWrapper<SystemUserDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return systemUserDAO.of()
                .wrapper(wrapper)
                .countMax(request.getLimit());
    }

    @Override
    public Integer deleteSystemUserById(Long id) {
        return this.deleteSystemUserByIdList(Lists.singleton(id));
    }

    @Override
    public Integer deleteSystemUserByIdList(List<Long> idList) {
        if (idList.contains(SecurityUtils.getLoginUserId())) {
            throw ErrorCode.UNSUPPOETED.exception();
        }
        // 查询用户列表
        List<SystemUserDO> userList = systemUserDAO.selectBatchIds(idList);
        Valid.notEmpty(userList, ErrorMessage.USER_ABSENT);
        // 添加日志参数
        idList = userList.stream()
                .map(SystemUserDO::getId)
                .collect(Collectors.toList());
        String username = userList.stream()
                .map(SystemUserDO::getUsername)
                .collect(Collectors.joining(Const.COMMA));
        OperatorLogs.add(OperatorLogs.USERNAME, username);
        // 删除用户
        int effect = systemUserDAO.deleteBatchIds(idList);
        log.info("SystemUserService-deleteSystemUserByIdList idList: {}, effect: {}", idList, effect);
        // 删除缓存 其他的缓存自动过期
        this.deleteUserCacheKey(userList);
        // 异步删除额外信息
        SpringHolder.getBean(SystemUserService.class)
                .deleteSystemUserListRelAsync(idList);
        return effect;
    }

    @Override
    @Async("asyncExecutor")
    public void deleteSystemUserListRelAsync(List<Long> idList) {
        log.info("SystemUserService-deleteSystemUserListRelAsync idList: {}", idList);
        // 删除角色关联
        systemUserRoleDAO.deleteByUserIdList(idList);
        // 删除操作日志
        operatorLogDAO.deleteByUserIdList(idList);
        // 删除用户收藏
        favoriteService.deleteFavoriteByUserIdList(idList);
        // 删除用户偏好
        preferenceService.deletePreferenceByUserIdList(idList);
        // 删除用户数据权限
        dataPermissionService.deleteByUserIdList(idList);
        // 删除用户拓展数据
        dataExtraService.deleteByUserIdList(idList);
        // 删除分组数据
        dataGroupService.deleteDataGroupByUserIdList(idList);
        // 删除命令片段
        commandSnippetApi.deleteByUserIdList(idList);
        // 删除路径标签
        pathBookmarkApi.deleteByUserIdList(idList);
    }

    @Override
    public void resetPassword(UserResetPasswordRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        // 查询用户
        SystemUserDO record = systemUserDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.USER_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.USERNAME, record.getUsername());
        // 修改密码
        SystemUserDO update = new SystemUserDO();
        update.setId(id);
        update.setPassword(Signatures.md5(request.getPassword()));
        int effect = systemUserDAO.updateById(update);
        log.info("SystemUserService-resetPassword record: {}, effect: {}", JSON.toJSONString(update), effect);
        // 删除登录失败次数缓存
        RedisUtils.delete(UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(record.getUsername()));
        // 删除登录缓存
        RedisUtils.scanKeysDelete(UserCacheKeyDefine.LOGIN_TOKEN.format(id, "*"));
        // 删除续签信息
        if (appAuthenticationConfig.getAllowRefresh()) {
            RedisUtils.scanKeysDelete(UserCacheKeyDefine.LOGIN_REFRESH.format(id, "*"));
        }
    }

    /**
     * 检查用户名否存在
     *
     * @param domain domain
     */
    private void checkUsernamePresent(SystemUserDO domain) {
        // 构造条件
        LambdaQueryWrapper<SystemUserDO> wrapper = systemUserDAO.wrapper()
                // 更新时忽略当前记录
                .ne(SystemUserDO::getId, domain.getId())
                .eq(SystemUserDO::getUsername, domain.getUsername());
        // 检查是否存在
        boolean present = systemUserDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.USERNAME_PRESENT);
    }

    /**
     * 检查花名是否存在
     *
     * @param domain domain
     */
    private void checkNicknamePresent(SystemUserDO domain) {
        // 构造条件
        LambdaQueryWrapper<SystemUserDO> wrapper = systemUserDAO.wrapper()
                // 更新时忽略当前记录
                .ne(SystemUserDO::getId, domain.getId())
                .eq(SystemUserDO::getNickname, domain.getNickname());
        // 检查是否存在
        boolean present = systemUserDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.NICKNAME_PRESENT);
    }

    /**
     * 删除主要用户缓存 其他的缓存自动过期
     *
     * @param userList userList
     */
    private void deleteUserCacheKey(List<SystemUserDO> userList) {
        Set<String> deleteKeys = new HashSet<>();
        // 用户列表缓存
        deleteKeys.add(UserCacheKeyDefine.USER_LIST.getKey());
        userList.forEach(s -> {
            Long id = s.getId();
            // 用户提示
            deleteKeys.add(TipsCacheKeyDefine.TIPS.format(id));
            // 用户信息缓存
            deleteKeys.add(UserCacheKeyDefine.USER_INFO.format(id));
            // 登录失败次数
            deleteKeys.add(UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(s.getUsername()));
            // 登录 token
            deleteKeys.addAll(RedisUtils.scanKeys(UserCacheKeyDefine.LOGIN_TOKEN.format(id, "*")));
            // 刷新 token
            deleteKeys.addAll(RedisUtils.scanKeys(UserCacheKeyDefine.LOGIN_REFRESH.format(id, "*")));
        });
        RedisUtils.delete(deleteKeys);
    }

    @Override
    public LambdaQueryWrapper<SystemUserDO> buildQueryWrapper(SystemUserQueryRequest request) {
        return systemUserDAO.wrapper()
                .eq(SystemUserDO::getId, request.getId())
                .like(SystemUserDO::getUsername, request.getUsername())
                .like(SystemUserDO::getNickname, request.getNickname())
                .like(SystemUserDO::getMobile, request.getMobile())
                .like(SystemUserDO::getEmail, request.getEmail())
                .eq(SystemUserDO::getStatus, request.getStatus());
    }

}
