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
package org.dromara.visor.module.monitor.enums;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.math.Numbers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * 指标单位枚举
 *
 * @author Jiahang Li
 * @version 1.1.0
 * @since 2025/8/14 10:15
 */
public enum MetricsUnitEnum {

    /**
     * 字节
     */
    BYTES {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            final String[] units = {"B", "KB", "MB", "GB", "TB"};
            if (BigDecimal.ZERO.compareTo(value) == 0) {
                return "0" + units[0];
            }
            BigDecimal v = value.abs();
            int i = 0;
            while (v.compareTo(N_1024) >= 0 && i < units.length - 1) {
                v = v.divide(N_1024, 10, RoundingMode.HALF_UP);
                i++;
            }
            option.setPrecision((i < 3) ? 0 : this.getPrecision(option));
            option.setSuffix(units[i]);
            BigDecimal signedValue = (value.signum() < 0) ? v.negate() : v;
            return this.formatNumber(signedValue, option);
        }

        @Override
        public String formatThreshold(BigDecimal value, FormatOptions option) {
            option.setSuffix("MB");
            option.setPrecision(4);
            return this.formatNumber(value, option);
        }

        @Override
        public BigDecimal getThresholdOriginalValue(BigDecimal value) {
            // MB > bytes
            return value.multiply(N_1024).multiply(N_1024);
        }
    },

    /**
     * 比特
     */
    BITS {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            final String[] units = {"b", "Kb", "Mb", "Gb"};
            if (BigDecimal.ZERO.compareTo(value) == 0) {
                return "0" + units[0];
            }
            BigDecimal v = value.abs().multiply(N_8);
            int i = 0;
            while (v.compareTo(N_1000) >= 0 && i < units.length - 1) {
                v = v.divide(N_1000, 10, RoundingMode.HALF_UP);
                i++;
            }
            option.setPrecision((i < 2) ? 0 : this.getPrecision(option));
            option.setSuffix(units[i]);
            BigDecimal signedValue = (value.signum() < 0) ? v.negate() : v;
            return this.formatNumber(signedValue, option);
        }

        @Override
        public String formatThreshold(BigDecimal value, FormatOptions option) {
            option.setSuffix("Mb");
            option.setPrecision(4);
            return this.formatNumber(value, option);
        }

        @Override
        public BigDecimal getThresholdOriginalValue(BigDecimal value) {
            // Mb > bytes
            return value.divide(N_8, 10, RoundingMode.HALF_UP)
                    .multiply(N_1000)
                    .multiply(N_1000);
        }
    },

    /**
     * 次数
     */
    COUNT {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            BigDecimal abs = value.abs();
            if (abs.compareTo(N_1M) >= 0) {
                option.setSuffix("M");
                return this.formatNumber(value.divide(N_1M, 10, RoundingMode.HALF_UP), option);
            } else if (abs.compareTo(N_1K) >= 0) {
                option.setSuffix("K");
                return this.formatNumber(value.divide(N_1K, 10, RoundingMode.HALF_UP), option);
            } else {
                return this.formatNumber(value, option);
            }
        }

        @Override
        public String formatThreshold(BigDecimal value, FormatOptions option) {
            option.setSuffix("");
            return this.formatNumber(value, option);
        }
    },

    /**
     * 秒
     */
    SECONDS {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            if (value.compareTo(N_3600) >= 0) {
                option.setSuffix("h");
                return this.formatNumber(value.divide(N_3600, 10, RoundingMode.HALF_UP), option) + "h";
            } else if (value.compareTo(N_60) >= 0) {
                option.setSuffix("m");
                return this.formatNumber(value.divide(N_60, 10, RoundingMode.HALF_UP), option) + "m";
            } else {
                option.setSuffix("s");
                return this.formatNumber(value, option) + "s";
            }
        }

        @Override
        public String formatThreshold(BigDecimal value, FormatOptions option) {
            option.setSuffix("s");
            return this.formatNumber(value, option);
        }
    },

    /**
     * 百分比
     */
    PER {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            option.setSuffix("%");
            return this.formatNumber(value, option);
        }
    },

    /**
     * 字节/秒
     */
    BYTES_S {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            return BYTES.format(value, option) + "/s";
        }

        @Override
        public String formatThreshold(BigDecimal value, FormatOptions option) {
            return BYTES.formatThreshold(value, option) + "/s";
        }

        @Override
        public BigDecimal getThresholdOriginalValue(BigDecimal value) {
            return BYTES.getThresholdOriginalValue(value);
        }
    },

    /**
     * 比特/秒
     */
    BITS_S {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            return BITS.format(value, option) + "ps";
        }

        @Override
        public String formatThreshold(BigDecimal value, FormatOptions option) {
            return BITS.formatThreshold(value, option) + "ps";
        }

        @Override
        public BigDecimal getThresholdOriginalValue(BigDecimal value) {
            return BITS.getThresholdOriginalValue(value);
        }
    },

    /**
     * 次/秒
     */
    COUNT_S {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            return COUNT.format(value, option) + "/s";
        }

        @Override
        public String formatThreshold(BigDecimal value, FormatOptions option) {
            option.setSuffix("/s");
            return this.formatNumber(value, option);
        }
    },

    /**
     * 文本
     */
    TEXT {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            return this.formatNumber(value, option);
        }
    },

    /**
     * 无单位
     */
    NONE {
        @Override
        public String format(BigDecimal value, FormatOptions option) {
            return this.formatNumber(value, option);
        }
    };

    // -------------------- BigDecimal 常量 --------------------

    private static final BigDecimal N_8 = BigDecimal.valueOf(8);
    private static final BigDecimal N_1000 = BigDecimal.valueOf(1000);
    private static final BigDecimal N_1024 = BigDecimal.valueOf(1024);
    private static final BigDecimal N_60 = BigDecimal.valueOf(60);
    private static final BigDecimal N_3600 = BigDecimal.valueOf(3600);
    private static final BigDecimal N_1K = BigDecimal.valueOf(1_000);
    private static final BigDecimal N_1M = BigDecimal.valueOf(1_000_000);

    // -------------------- 格式化指标 --------------------

    public String format(BigDecimal value) {
        FormatOptions options = new FormatOptions();
        options.setPrecision(2);
        return this.format(value, options);
    }

    public abstract String format(BigDecimal value, FormatOptions option);

    // -------------------- 格式化阈值 --------------------

    public String formatThreshold(BigDecimal value) {
        FormatOptions options = new FormatOptions();
        options.setPrecision(4);
        return this.formatThreshold(value, options);
    }

    public String formatThreshold(BigDecimal value, FormatOptions option) {
        return this.format(value, option);
    }

    /**
     * 获取阈值对应的原始值
     *
     * @param value value
     * @return original value
     */
    public BigDecimal getThresholdOriginalValue(BigDecimal value) {
        return value;
    }

    protected String formatNumber(BigDecimal value, FormatOptions option) {
        int precision = this.getPrecision(option);
        String formatted = Numbers.formatPrecision(value, precision, false);
        return formatted + Strings.def(option.getSuffix());
    }

    /**
     * 获取小数位
     *
     * @param option option
     */
    protected int getPrecision(FormatOptions option) {
        return Optional.ofNullable(option)
                .map(FormatOptions::getPrecision)
                .orElse(2);
    }

    public static MetricsUnitEnum of(String name) {
        if (name == null) {
            return NONE;
        }
        for (MetricsUnitEnum unit : values()) {
            if (unit.name().equals(name)) {
                return unit;
            }
        }
        return NONE;
    }

    // -------------------- 内部类 --------------------

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormatOptions {

        private Integer precision;

        private String suffix;

    }

}
