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

import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.time.Dates;
import org.dromara.visor.framework.common.entity.StatisticsRange;
import org.dromara.visor.framework.common.entity.chart.LineSingleChartData;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.convert.ExecLogConvert;
import org.dromara.visor.module.asset.convert.TerminalConnectLogConvert;
import org.dromara.visor.module.asset.dao.ExecJobDAO;
import org.dromara.visor.module.asset.dao.ExecLogDAO;
import org.dromara.visor.module.asset.dao.TerminalConnectLogDAO;
import org.dromara.visor.module.asset.define.cache.AssetStatisticsCacheKeyDefine;
import org.dromara.visor.module.asset.entity.domain.ExecJobDO;
import org.dromara.visor.module.asset.entity.domain.ExecLogDO;
import org.dromara.visor.module.asset.entity.domain.TerminalConnectLogDO;
import org.dromara.visor.module.asset.entity.po.ExecLogCountPO;
import org.dromara.visor.module.asset.entity.po.TerminalConnectLogCountPO;
import org.dromara.visor.module.asset.entity.vo.AssetWorkplaceStatisticsVO;
import org.dromara.visor.module.asset.entity.vo.ExecLogVO;
import org.dromara.visor.module.asset.entity.vo.TerminalConnectLogVO;
import org.dromara.visor.module.asset.enums.ExecSourceEnum;
import org.dromara.visor.module.asset.service.AssetStatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资产模块统计服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 20:47
 */
@Service
public class AssetStatisticsServiceImpl implements AssetStatisticsService {

    @Resource
    private ExecJobDAO execJobDAO;

    @Resource
    private ExecLogDAO execLogDAO;

    @Resource
    private TerminalConnectLogDAO terminalConnectLogDAO;

    @Override
    public AssetWorkplaceStatisticsVO getWorkplaceStatisticsData() {
        Long userId = SecurityUtils.getLoginUserId();
        // 读取缓存
        String cacheKey = AssetStatisticsCacheKeyDefine.WORKPLACE_DATA.format(userId, Dates.current(Dates.YMD2));
        AssetWorkplaceStatisticsVO data = RedisStrings.getJson(cacheKey, AssetStatisticsCacheKeyDefine.WORKPLACE_DATA);
        if (data == null) {
            // 查询执行的计划任务数量
            int execJobCount = execJobDAO.of()
                    .createWrapper()
                    .eq(ExecJobDO::getExecUserId, userId)
                    .then()
                    .count()
                    .intValue();
            // 查询批量执行次数图表
            LineSingleChartData execLogCountChart = this.getUserExecLogCountChart(userId, ExecSourceEnum.BATCH);
            List<Integer> execLogCountData = execLogCountChart.getData();
            int execLogCount = execLogCountData.stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            // 查询终端连接次数图表
            LineSingleChartData terminalConnectCountChart = this.getTerminalConnectCountChart(userId);
            List<Integer> terminalConnectCountData = terminalConnectCountChart.getData();
            int terminalConnectCount = terminalConnectCountData.stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            data = AssetWorkplaceStatisticsVO.builder()
                    .execJobCount(execJobCount)
                    .todayExecCommandCount(Lists.last(execLogCountData))
                    .weekExecCommandCount(execLogCount)
                    .todayTerminalConnectCount(Lists.last(terminalConnectCountData))
                    .weekTerminalConnectCount(terminalConnectCount)
                    .execCommandChart(execLogCountChart)
                    .terminalConnectChart(terminalConnectCountChart)
                    .build();
            // 设置缓存
            RedisStrings.setJson(cacheKey, AssetStatisticsCacheKeyDefine.WORKPLACE_DATA, data);
        }
        // 查询命令执行记录
        List<ExecLogVO> execLogList = execLogDAO.of()
                .createWrapper()
                .eq(ExecLogDO::getUserId, userId)
                .eq(ExecLogDO::getSource, ExecSourceEnum.BATCH.name())
                .orderByDesc(ExecLogDO::getId)
                .then()
                .limit(10)
                .list(ExecLogConvert.MAPPER::to);
        data.setExecLogList(execLogList);
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
    public LineSingleChartData getUserExecLogCountChart(Long userId, ExecSourceEnum source) {
        Date endTime = new Date();
        Date startTime = Dates.stream()
                .clearHms()
                .addDay(-6)
                .get();
        List<String> rangeDays = StatisticsRange.WEEK.getDateRanges(startTime);
        // 查询连接数量
        Map<String, Integer> countMap = execLogDAO.selectExecLogCount(userId, source.name(), startTime, endTime)
                .stream()
                .collect(Collectors.toMap(ExecLogCountPO::getExecDate, ExecLogCountPO::getCount));
        // 构建每天的数据
        List<Integer> data = rangeDays.stream()
                .map(s -> countMap.getOrDefault(s, 0))
                .collect(Collectors.toList());
        return LineSingleChartData.builder()
                .x(rangeDays)
                .data(data)
                .build();
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
