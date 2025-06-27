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
package org.dromara.visor.module.terminal.service.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.time.Dates;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.entity.chart.LineSingleChartData;
import org.dromara.visor.common.enums.StatisticsRange;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.terminal.convert.TerminalConnectLogConvert;
import org.dromara.visor.module.terminal.dao.TerminalConnectLogDAO;
import org.dromara.visor.module.terminal.define.cache.TerminalStatisticsCacheKeyDefine;
import org.dromara.visor.module.terminal.entity.domain.TerminalConnectLogDO;
import org.dromara.visor.module.terminal.entity.po.TerminalConnectLogCountPO;
import org.dromara.visor.module.terminal.entity.vo.TerminalConnectLogVO;
import org.dromara.visor.module.terminal.entity.vo.TerminalWorkplaceStatisticsVO;
import org.dromara.visor.module.terminal.service.TerminalStatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 终端模块统计服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 22:24
 */
@Slf4j
@Service
public class TerminalStatisticsServiceImpl implements TerminalStatisticsService {

    @Resource
    private TerminalConnectLogDAO terminalConnectLogDAO;

    @Override
    public TerminalWorkplaceStatisticsVO getWorkplaceStatisticsData() {
        Long userId = SecurityUtils.getLoginUserId();
        // 读取缓存
        String cacheKey = TerminalStatisticsCacheKeyDefine.WORKPLACE_DATA.format(userId, Dates.current(Dates.YMD2));
        TerminalWorkplaceStatisticsVO data = RedisStrings.getJson(cacheKey, TerminalStatisticsCacheKeyDefine.WORKPLACE_DATA);
        if (data == null) {
            // 查询终端连接次数图表
            LineSingleChartData terminalConnectCountChart = this.getTerminalConnectCountChart(userId);
            List<Integer> terminalConnectCountData = terminalConnectCountChart.getData();
            int terminalConnectCount = terminalConnectCountData.stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            data = TerminalWorkplaceStatisticsVO.builder()
                    .todayTerminalConnectCount(Lists.last(terminalConnectCountData))
                    .weekTerminalConnectCount(terminalConnectCount)
                    .terminalConnectChart(terminalConnectCountChart)
                    .build();
            // 设置缓存
            RedisStrings.setJson(cacheKey, TerminalStatisticsCacheKeyDefine.WORKPLACE_DATA, data);
        }
        // 查询终端连接记录
        List<TerminalConnectLogVO> connectList = terminalConnectLogDAO.of()
                .createWrapper()
                .eq(TerminalConnectLogDO::getUserId, userId)
                .orderByDesc(TerminalConnectLogDO::getId)
                .then()
                .limit(10)
                .list(TerminalConnectLogConvert.MAPPER::to);
        data.setTerminalConnectList(connectList);
        return data;
    }

    @Override
    public LineSingleChartData getTerminalConnectCountChart(Long userId) {
        Date endTime = new Date();
        Date startTime = Dates.stream()
                .clearHms()
                .addDay(-6)
                .get();
        List<String> rangeDays = StatisticsRange.WEEK.getDateRanges(startTime);
        // 查询连接数量
        Map<String, Integer> countMap = terminalConnectLogDAO.selectConnectLogUserCount(userId, startTime, endTime)
                .stream()
                .collect(Collectors.toMap(TerminalConnectLogCountPO::getConnectDate, TerminalConnectLogCountPO::getCount));
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
