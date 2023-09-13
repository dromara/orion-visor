package com.orion.ops.module.asset.entity.dto.host;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

/**
 * ssh 环境变量配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/13 16:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HostSshEnvConfig extends HashMap<String, Object> implements HostConfigContent {

}
