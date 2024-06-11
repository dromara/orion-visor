package com.orion.visor.launch;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * application 启动类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/19 16:55
 */
@SpringBootApplication(scanBasePackages = {"com.orion.visor.launch", "com.orion.visor.module"})
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
        @Override
        public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
            return definition.getBeanClassName();
        }
    }

}
