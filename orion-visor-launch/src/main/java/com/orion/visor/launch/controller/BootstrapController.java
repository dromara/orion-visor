package com.orion.visor.launch.controller;

import com.orion.visor.framework.web.core.annotation.RestWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 启动服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/19 17:08
 */
@Tag(name = "launch - 启动服务")
@RestWrapper
@RestController
@RequestMapping("/server/bootstrap")
public class BootstrapController {

    @Operation(summary = "健康检测")
    @GetMapping("/health")
    public String health() {
        return "ok";
    }

}
