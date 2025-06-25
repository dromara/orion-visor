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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.utils.time.Dates;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.entity.chart.LineSingleChartData;
import org.dromara.visor.common.enums.StatisticsRange;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.dao.OperatorLogDAO;
import org.dromara.visor.module.infra.dao.SystemUserDAO;
import org.dromara.visor.module.infra.define.cache.InfraStatisticsCacheKeyDefine;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.po.OperatorLogCountPO;
import org.dromara.visor.module.infra.entity.vo.InfraWorkplaceStatisticsVO;
import org.dromara.visor.module.infra.entity.vo.LoginHistoryVO;
import org.dromara.visor.module.infra.service.InfraStatisticsService;
import org.dromara.visor.module.infra.service.OperatorLogService;
import org.dromara.visor.module.infra.service.SystemMessageService;
import org.dromara.visor.module.infra.service.SystemUserManagementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基建模块统计服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 16:52
 */
@Slf4j
@Service
public class InfraStatisticsServiceImpl implements InfraStatisticsService {

    @Resource
    private SystemUserDAO systemUserDAO;

    @Resource
    private OperatorLogDAO operatorLogDAO;

    @Resource
    private OperatorLogService operatorLogService;

    @Resource
    private SystemMessageService systemMessageService;

    @Resource
    private SystemUserManagementService systemUserManagementService;

    @Override
    public InfraWorkplaceStatisticsVO getWorkplaceStatisticsData() {
        Long userId = SecurityUtils.getLoginUserId();
        // 读取缓存
        String cacheKey = InfraStatisticsCacheKeyDefine.WORKPLACE_DATA.format(userId, Dates.current(Dates.YMD2));
        InfraWorkplaceStatisticsVO data = RedisStrings.getJson(cacheKey, InfraStatisticsCacheKeyDefine.WORKPLACE_DATA);
        if (data == null) {
            // 查询用户操作日志图表
            LineSingleChartData operatorChart = this.getUserOperatorLogChart(userId);
            data = InfraWorkplaceStatisticsVO.builder()
                    .userId(userId)
                    .operatorChart(operatorChart)
                    .build();
            // 设置缓存
            RedisStrings.setJson(cacheKey, InfraStatisticsCacheKeyDefine.WORKPLACE_DATA, data);
        }
        // 查询用户信息
        SystemUserDO user = systemUserDAO.of()
                .createWrapper()
                .select(SystemUserDO::getId,
                        SystemUserDO::getUsername,
                        SystemUserDO::getNickname,
                        SystemUserDO::getLastLoginTime)
                .eq(SystemUserDO::getId, userId)
                .then()
                .getOne();
        data.setUsername(user.getUsername());
        data.setNickname(user.getNickname());
        data.setLastLoginTime(user.getLastLoginTime());
        // 查询未读消息数量
        Integer unreadMessageCount = systemMessageService.getUnreadSystemMessageCount(userId);
        data.setUnreadMessageCount(unreadMessageCount);
        // 查询当前登录会话数量
        Integer userSessionCount = systemUserManagementService.getUserSessionCount(userId);
        data.setUserSessionCount(userSessionCount);
        // 查询用户登录日志
        List<LoginHistoryVO> loginHistoryList = operatorLogService.getLoginHistory(user.getUsername(), 10);
        data.setLoginHistoryList(loginHistoryList);
        return data;
    }

    @Override
    public LineSingleChartData getUserOperatorLogChart(Long userId) {
        Date endTime = new Date();
        Date startTime = Dates.stream()
                .clearHms()
                .addDay(-6)
                .get();
        List<String> rangeDays = StatisticsRange.WEEK.getDateRanges(startTime);
        // 查询操作数量
        Map<String, Integer> countMap = operatorLogDAO.selectOperatorLogCount(userId, startTime, endTime)
                .stream()
                .collect(Collectors.toMap(OperatorLogCountPO::getOperatorDate, OperatorLogCountPO::getCount));
        // 构建每天的数据
        List<Integer> data = rangeDays.stream()
                .map(s -> countMap.getOrDefault(s, 0))
                .collect(Collectors.toList());
        return LineSingleChartData.builder()
                .x(rangeDays)
                .data(data)
                .build();
    }

}
