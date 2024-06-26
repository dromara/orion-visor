package com.orion.visor.framework.common.utils;

import com.orion.lang.constant.Const;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 配置工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/28 23:21
 */
public class ConfigUtils {

    private ConfigUtils() {
    }

    public static List<String> parseStringList(List<String> list) {
        return parseStringList(list, Function.identity());
    }

    /**
     * 解析配置 List<String:String[,]>
     *
     * @param list   list
     * @param mapper mapper
     * @return config
     */
    public static List<String> parseStringList(List<String> list, Function<String, String> mapper) {
        return Optional.ofNullable(list)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .map(s -> s.split(Const.COMMA))
                .flatMap(Arrays::stream)
                .map(String::trim)
                .map(mapper)
                .collect(Collectors.toList());
    }

}
