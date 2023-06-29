package com.orion.ops.launch.controller;

import com.orion.ops.framework.common.annotation.RestWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/19 17:08
 */
@RestWrapper
@RestController
@RequestMapping("/server/bootstrap")
public class BootstrapController {

    @Operation(summary = "检测心跳")
    @GetMapping("/health")
    public String health() {
        return "server ok";
    }

}
