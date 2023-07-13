package com.orion.ops.framework.common.entity;

import com.orion.lang.define.wrapper.IPageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 公共页码请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/12 23:14
 */
@Data
public class PageRequest implements IPageRequest {

    @Range(min = 1, max = 10000)
    @Schema(description = "页码")
    private int page;

    @Range(min = 1, max = 100)
    @Schema(description = "大小")
    private int limit;

}
