package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorCode;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.framework.redis.core.utils.RedisUtils;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.convert.SystemUserConvert;
import com.orion.ops.module.infra.dao.OperatorLogDAO;
import com.orion.ops.module.infra.dao.SystemUserDAO;
import com.orion.ops.module.infra.dao.SystemUserRoleDAO;
import com.orion.ops.module.infra.define.cache.TipsCacheKeyDefine;
import com.orion.ops.module.infra.define.cache.UserCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.request.user.*;
import com.orion.ops.module.infra.entity.vo.SystemUserVO;
import com.orion.ops.module.infra.enums.UserStatusEnum;
import com.orion.ops.module.infra.service.AuthenticationService;
import com.orion.ops.module.infra.service.FavoriteService;
import com.orion.ops.module.infra.service.PreferenceService;
import com.orion.ops.module.infra.service.SystemUserService;
import com.orion.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

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
    private SystemUserDAO systemUserDAO;

    @Resource
    private SystemUserRoleDAO systemUserRoleDAO;

    @Resource
    private OperatorLogDAO operatorLogDAO;

    @Resource
    private FavoriteService favoriteService;

    @Resource
    private PreferenceService preferenceService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Long createSystemUser(SystemUserCreateRequest request) {
        // 转换
        SystemUserDO record = SystemUserConvert.MAPPER.to(request);
        // 查询用户名是否存在
        this.checkUsernamePresent(record);
        // 查询花名是否存在
        this.checkNicknamePresent(record);
        // 插入
        int effect = systemUserDAO.insert(record);
        log.info("SystemUserService-createSystemUser effect: {}, record: {}", effect, JSON.toJSONString(record));
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
        return effect;
    }

    @Override
    public Integer updateUserStatus(SystemUserUpdateStatusRequest request) {
        Long id = request.getId();
        if (id.equals(SecurityUtils.getLoginUserId())) {
            throw ErrorCode.UNSUPPOETED.exception();
        }
        // 检查状态
        UserStatusEnum status = Valid.valid(UserStatusEnum::of, request.getStatus());
        if (!status.equals(UserStatusEnum.DISABLED) && !status.equals(UserStatusEnum.ENABLED)) {
            throw ErrorCode.BAD_REQUEST.exception();
        }
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
        // 如果之前是锁定则删除登录失败次数缓存
        if (UserStatusEnum.LOCKED.getStatus().equals(record.getStatus())) {
            redisTemplate.delete(UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(record.getUsername()));
        }
        // 更新缓存中的status
        RedisStrings.<LoginUser>processSetJson(UserCacheKeyDefine.USER_INFO, s -> {
            s.setStatus(request.getStatus());
        }, id);
        return effect;
    }

    @Override
    public SystemUserVO getSystemUserById(Long id) {
        // 查询
        SystemUserDO record = systemUserDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.USER_ABSENT);
        // 转换
        return SystemUserConvert.MAPPER.to(record);
    }

    @Override
    public List<SystemUserVO> getSystemUserByIdList() {
        // 查询
        List<SystemUserDO> records = systemUserDAO.selectList(null);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return SystemUserConvert.MAPPER.to(records);
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
        if (id.equals(SecurityUtils.getLoginUserId())) {
            throw ErrorCode.UNSUPPOETED.exception();
        }
        // 查询用户
        SystemUserDO record = systemUserDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.USER_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.USERNAME, record.getUsername());
        // 删除用户
        int effect = systemUserDAO.deleteById(id);
        log.info("SystemUserService-deleteSystemUserById id: {}, effect: {}", id, effect);
        // 异步删除额外信息
        SpringHolder.getBean(SystemUserService.class).deleteSystemUserRel(id);
        return effect;
    }

    @Override
    @Async("asyncExecutor")
    public void deleteSystemUserRel(Long id) {
        log.info("SystemUserService-deleteSystemUserRel id: {}", id);
        // 删除用户缓存 需要扫描的 key 让其自动过期
        redisTemplate.delete(Lists.of(
                // 用户缓存
                UserCacheKeyDefine.USER_INFO.format(id),
                // 用户提示
                TipsCacheKeyDefine.TIPS.format(id)
        ));
        // 删除角色关联
        systemUserRoleDAO.deleteByUserId(id);
        // 删除操作日志
        operatorLogDAO.deleteByUserId(id);
        // 删除用户收藏
        favoriteService.deleteFavoriteByUserId(id);
        // 删除用户偏好
        preferenceService.deletePreferenceByUserId(id);
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
        redisTemplate.delete(UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(record.getUsername()));
        // 删除登录缓存
        String loginKey = UserCacheKeyDefine.LOGIN_TOKEN.format(id, "*");
        Set<String> loginKeyList = RedisUtils.scanKeys(loginKey);
        if (!loginKeyList.isEmpty()) {
            redisTemplate.delete(loginKeyList);
        }
        // 删除续签信息
        if (AuthenticationService.allowRefresh) {
            String refreshKey = UserCacheKeyDefine.LOGIN_REFRESH.format(id, "*");
            Set<String> refreshKeyList = RedisUtils.scanKeys(refreshKey);
            if (!refreshKeyList.isEmpty()) {
                redisTemplate.delete(refreshKeyList);
            }
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

}
