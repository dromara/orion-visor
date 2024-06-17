package com.orion.visor.framework.common.utils;

import com.orion.ext.location.Region;
import com.orion.ext.location.region.LocationRegions;
import com.orion.visor.framework.common.constant.Const;

import java.util.HashMap;
import java.util.Map;

/**
 * ip 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 16:26
 */
public class IpUtils {

    private static final Map<String, String> CACHE = new HashMap<>();

    private IpUtils() {
    }

    /**
     * 获取 ip 位置
     *
     * @param ip ip
     * @return ip 位置
     */
    public static String getLocation(String ip) {
        if (ip == null) {
            return Const.CN_UNKNOWN;
        }
        // 查询缓存
        return CACHE.computeIfAbsent(ip, IpUtils::queryLocation);
    }

    /**
     * 查询 ip 位置
     *
     * @param ip ip
     * @return ip 位置
     */
    private static String queryLocation(String ip) {
        if (ip == null) {
            return Const.CN_UNKNOWN;
        }
        Region region;
        try {
            region = LocationRegions.getRegion(ip, 3);
        } catch (Exception e) {
            return Const.CN_UNKNOWN;
        }
        if (region != null) {
            String net = region.getNet();
            String province = region.getProvince();
            if (net.equals(Const.CN_INTRANET_IP)) {
                return net;
            }
            if (province.equals(Const.CN_UNKNOWN)) {
                return province;
            }
            StringBuilder location = new StringBuilder()
                    .append(region.getCountry())
                    .append(Const.DASHED)
                    .append(province)
                    .append(Const.DASHED)
                    .append(region.getCity());
            location.append(" (").append(net).append(')');
            return location.toString();
        }
        return Const.CN_UNKNOWN;
    }

}
