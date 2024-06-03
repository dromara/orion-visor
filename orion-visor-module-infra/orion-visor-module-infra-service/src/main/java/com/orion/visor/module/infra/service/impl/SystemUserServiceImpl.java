package com.orion.visor.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.spring.SpringHolder;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorCode;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.security.LoginUser;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.redis.core.utils.RedisMaps;
import com.orion.visor.framework.redis.core.utils.RedisStrings;
import com.orion.visor.framework.redis.core.utils.RedisUtils;
import com.orion.visor.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.infra.convert.SystemRoleConvert;
import com.orion.visor.module.infra.convert.SystemUserConvert;
import com.orion.visor.module.infra.dao.OperatorLogDAO;
import com.orion.visor.module.infra.dao.SystemRoleDAO;
import com.orion.visor.module.infra.dao.SystemUserDAO;
import com.orion.visor.module.infra.dao.SystemUserRoleDAO;
import com.orion.visor.module.infra.define.RoleDefine;
import com.orion.visor.module.infra.define.cache.TipsCacheKeyDefine;
import com.orion.visor.module.infra.define.cache.UserCacheKeyDefine;
import com.orion.visor.module.infra.define.config.AppAuthenticationConfig;
import com.orion.visor.module.infra.entity.domain.SystemRoleDO;
import com.orion.visor.module.infra.entity.domain.SystemUserDO;
import com.orion.visor.module.infra.entity.dto.UserInfoDTO;
import com.orion.visor.module.infra.entity.request.user.*;
import com.orion.visor.module.infra.entity.vo.SystemUserVO;
import com.orion.visor.module.infra.enums.UserStatusEnum;
import com.orion.visor.module.infra.service.*;
import lombok.extern.slf4j.Slf4j;
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
        // 构造条件
        LambdaQueryWrapper<SystemUserDO> wrapper = systemUserDAO.wrapper()
                .eq(SystemUserDO::getId, request.getId())
                .like(SystemUserDO::getUsername, request.getUsername())
                .like(SystemUserDO::getNickname, request.getNickname())
                .like(SystemUserDO::getMobile, request.getMobile())
                .like(SystemUserDO::getEmail, request.getEmail())
                .eq(SystemUserDO::getStatus, request.getStatus());
        // 查询
        return systemUserDAO.of(wrapper)
                .page(request)
                .dataGrid(SystemUserConvert.MAPPER::to);
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
        // TODO snippet
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

    @Override
    public boolean isAdminUser(Long userId) {
        return systemRoleDAO.getRoleIdByUserIdAndRoleCode(userId, RoleDefine.ADMIN_CODE) != null;
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
     * @param userList
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

}
