package com.orion.visor.module.asset.handler.host.terminal.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * sftp 文件响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/6 13:57
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SftpFileVO {

    /**
     * 名称
     */
    private String name;

    /**
     * 绝对路径
     */
    private String path;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件大小(byte)
     */
    private Long size;

    /**
     * 属性
     */
    private String attr;

    /**
     * 是否为目录
     */
    private Boolean isDir;

    /**
     * 10进制表现的8进制权限
     */
    private Integer permission;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 组id
     */
    private Integer gid;

    /**
     * 更新时间
     */
    private Date modifyTime;

}
