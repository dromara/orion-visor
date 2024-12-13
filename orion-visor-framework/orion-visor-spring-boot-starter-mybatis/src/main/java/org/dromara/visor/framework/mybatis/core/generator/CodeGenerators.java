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
package org.dromara.visor.framework.mybatis.core.generator;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.ansi.AnsiAppender;
import cn.orionsec.kit.lang.utils.ansi.style.AnsiFont;
import cn.orionsec.kit.lang.utils.ansi.style.color.AnsiForeground;
import cn.orionsec.kit.lang.utils.ext.yml.YmlExt;
import org.dromara.visor.framework.mybatis.core.generator.core.CodeGenerator;
import org.dromara.visor.framework.mybatis.core.generator.template.Table;
import org.dromara.visor.framework.mybatis.core.generator.template.Template;

import java.io.File;

/**
 * 代码生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/4/20 10:33
 */
public class CodeGenerators {

    public static void main(String[] args) {
        // 输出路径
        String outputDir = "D:/MP/";
        // 作者
        String author = Const.ORION_AUTHOR;
        // 模块
        String module = "infra";
        // 生成的表
        Table[] tables = {
                // Template.create("dict_key", "字典配置项", "dict")
                //         .enableProviderApi()
                //         .disableUnitTest()
                //         .cache("dict:keys", "字典配置项")
                //         .expire(8, TimeUnit.HOURS)
                //         .vue("system", "dict-key")
                //         .enableRowSelection()
                //         .enableCardView()
                //         .enableDrawerForm()
                //         .dict("dictValueType", "value_type")
                //         .comment("字典值类型")
                //         .fields("STRING", "INTEGER", "DECIMAL", "BOOLEAN", "COLOR")
                //         .labels("字符串", "整数", "小数", "布尔值", "颜色")
                //         .color("blue", "gray", "red", "green", "white")
                //         .valueUseFields()
                //         .build(),
                // Template.create("exec_template_host", "执行模板主机", "exec")
                //         .enableProviderApi()
                //         .cache("sl", "22")
                //         .vue("exec", "exec-template-host")
                //         .build(),
                Template.create("system_message", "系统消息", "message")
                        .disableUnitTest()
                        .enableProviderApi()
                        .vue("system", "message")
                        .dict("messageClassify", "classify", "messageClassify")
                        .comment("消息分类")
                        .fields("NOTICE", "TODO")
                        .labels("通知", "待办")
                        .valueUseFields()
                        .dict("messageType", "type", "messageType")
                        .comment("消息类型")
                        .fields("EXEC_FAILED", "UPLOAD_FAILED")
                        .labels("执行失败", "上传失败")
                        .extra("tagLabel", "执行失败", "上传失败")
                        .extra("tagVisible", true, true)
                        .extra("tagColor", "red", "red")
                        .valueUseFields()
                        .build(),
        };
        // jdbc 配置 - 使用配置文件
        File yamlFile = new File("orion-visor-launch/src/main/resources/application-dev.yaml");
        YmlExt yaml = YmlExt.load(yamlFile);
        String url = yaml.getValue("spring.datasource.druid.url");
        String username = yaml.getValue("spring.datasource.druid.username");
        String password = yaml.getValue("spring.datasource.druid.password");

        // 执行
        runGenerator(outputDir, author,
                tables, module,
                url, username, password);
    }

    /**
     * 代码生成
     */
    private static void runGenerator(String outputDir,
                                     String author,
                                     Table[] tables,
                                     String module,
                                     String url,
                                     String username,
                                     String password) {
        // 执行代码生成
        CodeGenerator.builder()
                .outputDir(outputDir)
                .author(author)
                .tables(tables)
                .module(module)
                .url(url)
                .username(username)
                .password(password)
                .build()
                .exec();

        // 打印提示信息
        printTips();
    }

    /**
     * 打印提示信息
     */
    private static void printTips() {
        String line = AnsiAppender.create()
                .append(AnsiForeground.BRIGHT_GREEN.and(AnsiFont.BOLD), "\n:: 代码生成完毕 ^_^ ::\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- 后端代码复制后请先 clean 模块父工程 (clean dependencies, build dependencies, clean module)\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- 后端代码需要自行修改缓存逻辑\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- 后端代码修改完成后请先执行单元测试检测是否正常\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- vue 代码需要注意同一模块的 router 需要自行合并\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- vue 枚举需要自行更改数据类型\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- 菜单 sql 执行完成后 需要在系统菜单页面刷新缓存\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- 字典 sql 执行完成后 需要在字典配置项页面刷新缓存\n")
                .toString();
        System.out.print(line);
    }

}
