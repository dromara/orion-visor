/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.framework.test.core.utils;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.random.Randoms;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对象生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/23 14:13
 */
public class EntityRandoms {

    private EntityRandoms() {
    }

    private static final int RANDOM_STRING_LENGTH = 5;

    private static final int RANDOM_INT_MAX = 10;

    private static final int RANDOM_COLLECTION_LENGTH = 5;

    private static final String DELETED = "deleted";

    private static final PodamFactory FACTORY = new PodamFactoryImpl();

    static {
        // 字符串
        FACTORY.getStrategy().addOrReplaceTypeManufacturer(String.class, (dataProviderStrategy, attributeMetadata, map) -> Randoms.randomLetter(RANDOM_STRING_LENGTH));
        // Integer
        FACTORY.getStrategy().addOrReplaceTypeManufacturer(Integer.class, (dataProviderStrategy, attributeMetadata, map) -> Randoms.randomInt(0, RANDOM_INT_MAX));
        // Boolean
        FACTORY.getStrategy().addOrReplaceTypeManufacturer(Boolean.class, (dataProviderStrategy, attributeMetadata, map) -> {
            if (DELETED.equals(attributeMetadata.getAttributeName())) {
                return false;
            }
            return Randoms.randomBoolean();
        });
        // Collection
        FACTORY.getStrategy().setDefaultNumberOfCollectionElements(RANDOM_COLLECTION_LENGTH);
    }

    @SafeVarargs
    public static <T> T random(Class<T> clazz, Consumer<T>... consumers) {
        T e = FACTORY.manufacturePojo(clazz);
        if (Arrays1.isNotEmpty(consumers)) {
            Arrays.stream(consumers).forEach(consumer -> consumer.accept(e));
        }
        return e;
    }

    @SafeVarargs
    public static <T> T random(Class<T> clazz, Type type, Consumer<T>... consumers) {
        T e = FACTORY.manufacturePojo(clazz, type);
        if (Arrays1.isNotEmpty(consumers)) {
            Arrays.stream(consumers).forEach(consumer -> consumer.accept(e));
        }
        return e;
    }

    @SafeVarargs
    public static <T> Set<T> randomSet(Class<T> clazz, Consumer<T>... consumers) {
        return Stream.iterate(0, i -> i)
                .limit(Randoms.randomInt(1, RANDOM_COLLECTION_LENGTH))
                .map(o -> random(clazz, consumers))
                .collect(Collectors.toSet());
    }

    @SafeVarargs
    public static <T> List<T> randomList(Class<T> clazz, Consumer<T>... consumers) {
        return Stream.iterate(0, i -> i)
                .limit(Randoms.randomInt(1, RANDOM_COLLECTION_LENGTH))
                .map(o -> random(clazz, consumers))
                .collect(Collectors.toList());
    }

}
