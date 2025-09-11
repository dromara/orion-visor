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
package org.dromara.visor.module.monitor.convert;

import org.dromara.visor.module.monitor.entity.domain.MonitorMetricsDO;
import org.dromara.visor.module.monitor.entity.dto.MonitorMetricsCacheDTO;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsCreateRequest;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsQueryRequest;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.MonitorMetricsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 监控指标 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-12 21:31
 */
@Mapper
public interface MonitorMetricsConvert {

    MonitorMetricsConvert MAPPER = Mappers.getMapper(MonitorMetricsConvert.class);

    MonitorMetricsDO to(MonitorMetricsCreateRequest request);

    MonitorMetricsDO to(MonitorMetricsUpdateRequest request);

    MonitorMetricsDO to(MonitorMetricsQueryRequest request);

    MonitorMetricsVO to(MonitorMetricsDO domain);

    List<MonitorMetricsVO> to(List<MonitorMetricsDO> list);

    MonitorMetricsVO to(MonitorMetricsCacheDTO cache);

    MonitorMetricsCacheDTO toCache(MonitorMetricsDO domain);

}
