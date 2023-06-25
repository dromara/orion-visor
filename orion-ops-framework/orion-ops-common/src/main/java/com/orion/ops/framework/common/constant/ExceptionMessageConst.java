package com.orion.ops.framework.common.constant;

/**
 * 消息常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/6/4 18:26
 */
public interface ExceptionMessageConst {

    String INVALID_PARAM = "非法参数";

    String OPERATOR_ERROR = "操作失败";

    String HTTP_API = "api 调用异常";

    String NETWORK_FLUCTUATION = "当前环境网路波动";

    String OPEN_TEMPLATE_ERROR = "模板解析失败 请检查模板和密码";

    String PARSE_TEMPLATE_DATA_ERROR = "模板解析失败 请检查模板数据";

    String REPOSITORY_OPERATOR_ERROR = "应用版本仓库操作执行失败";

    String TASK_ERROR = "任务执行异常";

    String CONNECT_ERROR = "建立连接失败";

    String TIMEOUT_ERROR = "处理超时";

    String INTERRUPT_ERROR = "操作中断";

    String UNSAFE_OPERATOR = "不安全的操作";

    String ENCRYPT_ERROR = "数据加密异常";

    String DECRYPT_ERROR = "数据解密异常";

    String EXCEPTION_MESSAGE = "系统异常";

    String IO_EXCEPTION_MESSAGE = "网络异常";

    String SQL_EXCEPTION_MESSAGE = "数据异常";

    String FILE_TOO_LARGE = "文件过大";

    String ERROR_EXPRESSION = "表达式错误";

}
