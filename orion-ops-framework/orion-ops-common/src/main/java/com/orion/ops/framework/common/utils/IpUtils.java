package com.orion.ops.framework.common.utils;

import com.orion.ext.location.Region;
import com.orion.ext.location.region.LocationRegions;
import com.orion.ops.framework.common.constant.Const;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 16:26
 */
public class IpUtils {

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
            return Const.UNKNOWN;
        }
        Region region;
        try {
            region = LocationRegions.getRegion(ip, 3);
        } catch (Exception e) {
            return Const.UNKNOWN;
        }
        if (region != null) {
            String net = region.getNet();
            String province = region.getProvince();
            if (net.equals(Const.INTRANET_IP)) {
                return net;
            }
            if (province.equals(Const.UNKNOWN)) {
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
        return Const.UNKNOWN;
    }

}
