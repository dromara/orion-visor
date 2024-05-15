package com.orion.visor.framework.common.entity;

import com.orion.lang.define.wrapper.IPageRequest;
import com.orion.visor.framework.common.validator.group.Page;
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
@Schema(description = "公共页码请求")
public class PageRequest implements IPageRequest {

    @Range(min = 1, max = 10000, groups = Page.class)
    @Schema(description = "页码")
    private int page;

    @Range(min = 1, max = 100, groups = Page.class)
    @Schema(description = "大小")
    private int limit;

}
