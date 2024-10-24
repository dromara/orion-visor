/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.FavoriteDO;
import com.orion.visor.module.infra.entity.request.favorite.FavoriteOperatorRequest;
import com.orion.visor.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.visor.module.infra.entity.vo.FavoriteVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 收藏 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
@Mapper
public interface FavoriteConvert {

    FavoriteConvert MAPPER = Mappers.getMapper(FavoriteConvert.class);

    FavoriteDO to(FavoriteOperatorRequest request);

    FavoriteDO to(FavoriteQueryRequest request);

    FavoriteVO to(FavoriteDO domain);

}
