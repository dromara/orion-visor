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
package org.dromara.visor.launch;

import cn.orionsec.kit.lang.utils.Strings;
import org.dromara.visor.framework.common.constant.Const;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Optional;

/**
 * application 启动类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/19 16:55
 */
@SpringBootApplication(scanBasePackages = {"org.dromara.visor.launch", "org.dromara.visor.module"})
public class LaunchApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(LaunchApplication.class)
                .beanNameGenerator(new CustomBeanNameGenerator())
                .run(args);
    }

    /**
     * 自定义 bean 名称生成器
     */
    public static class CustomBeanNameGenerator implements BeanNameGenerator {

        private static final String BEAN_ANNOTATION_CLASS_NAME = "org.springframework.stereotype.Component";

        @Override
        public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
            // 兼容注解自定义名称
            if (definition instanceof AnnotatedBeanDefinition) {
                AnnotationMetadata metadata = ((AnnotatedBeanDefinition) definition).getMetadata();
                // 处理自定义 bean 名称
                return Optional.of(metadata)
                        .map(s -> s.getAnnotationAttributes(BEAN_ANNOTATION_CLASS_NAME))
                        .map(s -> s.get(Const.VALUE))
                        .map(Object::toString)
                        .filter(Strings::isNotBlank)
                        .orElseGet(definition::getBeanClassName);
            } else {
                // 非注解形式默认使用默认名称
                return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
            }
        }
    }

}
