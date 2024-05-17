package com.orion.visor.module.infra.define;

import com.orion.lang.utils.Strings;
import com.orion.visor.module.infra.enums.MessageClassifyEnum;

import java.util.Map;

/**
 * 系统消息定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/14 17:06
 */
public interface SystemMessageDefine {

    /**
     * @return 消息分类
     */
    MessageClassifyEnum getClassify();

    /**
     * @return 消息类型
     */
    String getType();

    /**
     * @return 标题
     */
    String getTitle();

    /**
     * @return 内容
     */
    String getContent();

    /**
     * 格式化内容
     *
     * @param params params
     * @return content
     */
    default String formatContent(Map<String, Object> params) {
        return Strings.format(this.getContent(), params);
    }

}
