package com.orion.ops.framework.mybatis.core.generator.core;

import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.orion.ops.framework.mybatis.core.generator.template.Table;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义文件过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/30 11:08
 */
public class CustomFileFilter {

    /**
     * 表
     */
    private final Table table;

    /**
     * 原始文件
     */
    private final List<CustomFile> originCustomerFile;

    public CustomFileFilter(Table table, List<CustomFile> originCustomerFile) {
        this.table = table;
        this.originCustomerFile = originCustomerFile;
    }

    /**
     * 执行过滤
     *
     * @return clone filter files
     */
    public List<CustomFile> doFilter() {
        // 生成文件副本
        List<CustomFile> files = originCustomerFile.stream().map(s ->
                new CustomFile.Builder()
                        .enableFileOverride()
                        .templatePath(s.getTemplatePath())
                        .filePath(s.getFilePath())
                        .fileName(s.getFileName())
                        .packageName(s.getPackageName())
                        .build())
                .collect(Collectors.toList());
        // 不生成对外 api 文件
        if (!table.isEnableProviderApi()) {
            files.removeIf(file -> isServerProviderFile(file.getTemplatePath()));
            // 不生成对外 api 单元测试文件
            if (table.isEnableUnitTest()) {
                files.removeIf(file -> isServerProviderTestFile(file.getTemplatePath()));
            }
        }
        // 不生成单元测试文件
        if (!table.isEnableUnitTest()) {
            files.removeIf(file -> isServerUnitTestFile(file.getTemplatePath()));
        }
        // 不生成缓存文件
        if (!table.isEnableCache()) {
            files.removeIf(file -> isServerCacheFile(file.getTemplatePath()));
        }
        // 不生成导出文件
        if (!table.isEnableExport()) {
            files.removeIf(file -> isExportFile(file.getTemplatePath()));
        }
        // 不生成操作日志文件
        if (!table.isEnableOperatorLog()) {
            files.removeIf(file -> isOperatorLogFile(file.getTemplatePath()));
        }
        // 不生成 vue 文件
        if (!table.isEnableVue()) {
            files.removeIf(file -> isVueFile(file.getTemplatePath()));
            // 不生成菜单 sql 文件
            files.removeIf(file -> isMenuDataFile(file.getTemplatePath()));
        } else {
            // form
            if (!table.isEnableDrawerForm()) {
                // 不生成抽屉表单
                files.removeIf(file -> isVueFormDrawerFile(file.getTemplatePath()));
            } else {
                // 不生成模态框表单
                files.removeIf(file -> isVueFormModalFile(file.getTemplatePath()));
            }
            // 不生成卡片文件
            if (!table.isEnableCardView()) {
                files.removeIf(file -> isVueCardViewFile(file.getTemplatePath()));
            }
        }
        return files;
    }

    /**
     * 是否为后端文件
     *
     * @param templatePath templatePath
     * @return 是否为后端文件
     */
    public static boolean isServerFile(String templatePath) {
        return templatePath.contains("orion-server");
    }

    /**
     * 是否为后端 provider 文件
     *
     * @param templatePath templatePath
     * @return 是否为后端 provider 文件
     */
    public static boolean isServerProviderFile(String templatePath) {
        return templatePath.contains("orion-server-provider");
    }

    /**
     * 是否为后端 provider 单元测试文件
     *
     * @param templatePath templatePath
     * @return 是否为后端 provider 单元测试文件
     */
    public static boolean isServerProviderTestFile(String templatePath) {
        return templatePath.contains("orion-server-test-api");
    }

    /**
     * 是否为后端单元测试文件
     *
     * @param templatePath templatePath
     * @return 是否为后端单元测试文件
     */
    public static boolean isServerUnitTestFile(String templatePath) {
        return templatePath.contains("orion-server-test");
    }

    /**
     * 是否为后端缓存文件
     *
     * @param templatePath templatePath
     * @return 是否为后端缓存文件
     */
    public static boolean isServerCacheFile(String templatePath) {
        return templatePath.contains("orion-server-module-cache");
    }

    /**
     * 是否为导出文件
     *
     * @param templatePath templatePath
     * @return 是否为导出文件
     */
    public static boolean isExportFile(String templatePath) {
        return templatePath.contains("orion-server-module-entity-export");
    }

    /**
     * 是否为操作日志文件
     *
     * @param templatePath templatePath
     * @return 是否为操作日志文件
     */
    public static boolean isOperatorLogFile(String templatePath) {
        return templatePath.contains("orion-server-module-operator-key-define");
    }

    /**
     * 是否为菜单 sql 文件
     *
     * @param templatePath templatePath
     * @return 是否为菜单 sql 文件
     */
    public static boolean isMenuDataFile(String templatePath) {
        return templatePath.contains("orion-sql-menu");
    }

    /**
     * 是否为 sql ddl 文件
     *
     * @param templatePath templatePath
     * @return 是否为数据库数据文件
     */
    public static boolean isDataSqlFile(String templatePath) {
        return templatePath.contains("orion-sql-menu") ||
                templatePath.contains("orion-sql-dict");
    }

    /**
     * 是否为 vue 文件
     *
     * @param templatePath templatePath
     * @return 是否为 vue 文件
     */
    public static boolean isVueFile(String templatePath) {
        return templatePath.contains("orion-vue-");
    }

    /**
     * 是否为抽屉表单文件
     *
     * @param templatePath templatePath
     * @return 是否为抽屉表单文件
     */
    public static boolean isVueFormDrawerFile(String templatePath) {
        return templatePath.contains("orion-vue-views-components-form-drawer");
    }

    /**
     * 是否为模态框表单文件
     *
     * @param templatePath templatePath
     * @return 是否为模态框表单文件
     */
    public static boolean isVueFormModalFile(String templatePath) {
        return templatePath.contains("orion-vue-views-components-form-modal");
    }

    /**
     * 是否为卡片视图文件
     *
     * @param templatePath templatePath
     * @return 是否为卡片视图文件
     */
    public static boolean isVueCardViewFile(String templatePath) {
        return templatePath.contains("orion-vue-views-components-card-list") ||
                templatePath.contains("orion-vue-views-types-card.fields");
    }

}
