package com.orion.ops.framework.mybatis.core.generator;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.ansi.AnsiAppender;
import com.orion.lang.utils.ansi.style.AnsiFont;
import com.orion.lang.utils.ansi.style.color.AnsiForeground;
import com.orion.lang.utils.ext.yml.YmlExt;
import com.orion.ops.framework.mybatis.core.generator.core.CodeGenerator;
import com.orion.ops.framework.mybatis.core.generator.template.Table;
import com.orion.ops.framework.mybatis.core.generator.template.Template;

import java.io.File;
import java.util.concurrent.TimeUnit;

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
        String module = "asset";
        // 生成的表
        Table[] tables = {
                // Template.create("dict_key", "字典配置项", "dict")
                //         .enableProviderApi()
                //         .disableUnitTest()
                //         .cache("dict:keys", "字典配置项")
                //         .expire(1, TimeUnit.DAYS)
                //         .vue("system", "dict-key")
                //         .enableRowSelection()
                //         .enableCardView()
                //         .enableDrawerForm()
                //         .dict("dictValueType", "value_type")
                //         .fields("STRING", "INTEGER", "DECIMAL", "BOOLEAN", "COLOR")
                //         .labels("字符串", "整数", "小数", "布尔值", "颜色")
                //         .color("blue", "gray", "red", "green", "white")
                //         .valueUseFields()
                //         .build(),
                Template.create("command_snippet", "命令片段", "command")
                        .disableUnitTest()
                        .cache("command:snippet:group:{}", "命令片段 ${userId}")
                        .expire(1, TimeUnit.DAYS)
                        .vue("asset", "command-snippet")
                        .enableDrawerForm()
                        .build(),
        };
        // jdbc 配置 - 使用配置文件
        File yamlFile = new File("orion-ops-launch/src/main/resources/application-dev.yaml");
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
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- 后端代码复制后请先 clean 父工程\n")
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
