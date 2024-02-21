package com.orion.ops.module.asset.handler.host.terminal.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "SftpFileResponse", description = "sftp 文件响应 实体对象")
public class SftpFileVO {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "绝对路径")
    private String path;

    @Schema(description = "文件后缀")
    private String suffix;

    @Schema(description = "文件大小(byte)")
    private Long size;

    @Schema(description = "属性")
    private String attr;

    @Schema(description = "是否为目录")
    private Boolean isDir;

    @Schema(description = "10进制表现的8进制权限")
    private Integer permission;

    @Schema(description = "用户id")
    private Integer uid;

    @Schema(description = "组id")
    private Integer gid;

    @Schema(description = "更新时间")
    private Date modifyTime;

}
