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
package org.dromara.visor.common.enums;

import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.time.DateStream;
import cn.orionsec.kit.lang.utils.time.Dates;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计区间枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 14:02
 */
public enum StatisticsRange {

    /**
     * 当天
     */
    TODAY {
        @Override
        public Date getRangeEndTime(Date startTime) {
            return DateStream.of(startTime)
                    .dayEnd()
                    .date();
        }

        @Override
        public List<String> getDateRanges(Date startTime) {
            return Lists.singleton(Dates.format(startTime, Dates.YMD));
        }
    },

    /**
     * 日视图
     */
    DAY {
        @Override
        public Date getRangeEndTime(Date startTime) {
            return DateStream.of(startTime)
                    .dayEnd()
                    .date();
        }

        @Override
        public List<String> getDateRanges(Date startTime) {
            return Lists.singleton(Dates.format(startTime, Dates.YMD));
        }
    },

    /**
     * 周视图
     */
    WEEK {
        @Override
        public Date getRangeEndTime(Date startTime) {
            return DateStream.of(startTime)
                    .addDay(7)
                    .dayEnd()
                    .date();
        }

        @Override
        public List<String> getDateRanges(Date startTime) {
            return Arrays.stream(Dates.getIncrementDayDates(startTime, 1, 7))
                    .map(s -> Dates.format(s, Dates.YMD))
                    .collect(Collectors.toList());
        }
    },

    /**
     * 月视图
     */
    MONTH {
        @Override
        public Date getRangeEndTime(Date startTime) {
            return DateStream.of(startTime)
                    .addMonth(1)
                    .dayEnd()
                    .date();
        }

        @Override
        public List<String> getDateRanges(Date startTime) {
            int monthLastDay = Dates.getMonthLastDay(startTime);
            return Arrays.stream(Dates.getIncrementDayDates(startTime, 1, monthLastDay - 1))
                    .map(s -> Dates.format(s, Dates.YMD))
                    .collect(Collectors.toList());
        }
    },

    ;

    /**
     * 获取区间结束时间
     *
     * @param startTime startTime
     * @return end
     */
    public abstract Date getRangeEndTime(Date startTime);

    /**
     * 获取时间区间
     *
     * @param startTime startTime
     * @return ranges
     */
    public abstract List<String> getDateRanges(Date startTime);

    public static StatisticsRange of(String type) {
        if (type == null) {
            return TODAY;
        }
        for (StatisticsRange value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return TODAY;
    }

}
