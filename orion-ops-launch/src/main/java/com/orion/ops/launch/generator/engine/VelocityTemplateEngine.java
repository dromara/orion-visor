/*
 * Copyright (c) 2011-2022, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.ops.launch.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.orion.lang.define.collect.MultiLinkedHashMap;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.VariableStyles;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.reflect.BeanMap;
import com.orion.lang.utils.reflect.Fields;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.OrionOpsProConst;
import com.orion.ops.launch.generator.template.Table;
import com.orion.ops.launch.generator.template.VueEnum;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 代码生成器 Velocity 引擎
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/4/20 10:33
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine {

    private final Map<String, Table> tables;

    private VelocityEngine velocityEngine;

    public VelocityTemplateEngine(Table[] tables) {
        this.tables = Arrays.stream(tables)
                .collect(Collectors.toMap(Table::getTableName, Function.identity()));
    }

    {
        try {
            Class.forName("org.apache.velocity.util.DuckType");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Velocity 1.x is outdated, please upgrade to 2.x or later.");
        }
    }

    @Override
    @NotNull
    public VelocityTemplateEngine init(@NotNull ConfigBuilder configBuilder) {
        if (velocityEngine == null) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOAD_PATH_KEY, ConstVal.VM_LOAD_PATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode", StringPool.TRUE);
            this.velocityEngine = new VelocityEngine(p);
        }
        return this;
    }

    @Override
    public void writer(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, @NotNull File outputFile) throws Exception {
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);
             BufferedWriter writer = new BufferedWriter(ow)) {
            template.merge(new VelocityContext(objectMap), writer);
        }
        LOGGER.debug("模板: " + templatePath + "; 文件: " + outputFile);
    }

    @Override
    @NotNull
    public String templateFilePath(@NotNull String filePath) {
        final String dotVm = ".vm";
        return filePath.endsWith(dotVm) ? filePath : filePath + dotVm;
    }

    @Override
    protected void outputCustomFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 创建自定义文件副本文件
        customFiles = this.createCustomFilesBackup(customFiles, tableInfo);
        // 添加表元数据
        this.addTableMeta(tableInfo, objectMap);
        // 替换自定义包名
        this.replacePackageName(customFiles, tableInfo, objectMap);
        // 添加注释元数据
        this.addApiCommentMeta(tableInfo, objectMap);
        // 生成后端文件
        this.generatorServerFile(customFiles, tableInfo, objectMap);
        // 生成前端文件
        this.generatorVueFile(customFiles, tableInfo, objectMap);
    }

    /**
     * 创建自定义文件副本对象
     * <p>
     * - 根据类型进行移除不需要生成的模板
     *
     * @param originCustomerFile originCustomerFile
     * @param tableInfo          tableInfo
     * @return backup
     */
    private List<CustomFile> createCustomFilesBackup(@NotNull List<CustomFile> originCustomerFile,
                                                     @NotNull TableInfo tableInfo) {
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
        // 获取 table
        Table table = tables.get(tableInfo.getName());
        // 不生成对外 api 文件
        if (!table.isEnableProviderApi()) {
            files.removeIf(file -> this.isServerProviderFile(file.getTemplatePath()));
            // 不生成对外 api 单元测试文件
            if (table.isEnableUnitTest()) {
                files.removeIf(file -> this.isServerProviderTestFile(file.getTemplatePath()));
            }
        }
        // 不生成单元测试文件
        if (!table.isEnableUnitTest()) {
            files.removeIf(file -> this.isServerUnitTestFile(file.getTemplatePath()));
        }
        // 不生成缓存文件
        if (!table.isEnableCache()) {
            files.removeIf(file -> this.isServerCacheFile(file.getTemplatePath()));
        }
        // 不生成 vue 文件
        if (!table.isEnableVue()) {
            files.removeIf(file -> this.isVueFile(file.getTemplatePath()));
        }
        return files;
    }

    /**
     * 插入表元数据
     *
     * @param tableInfo tableInfo
     * @param objectMap objectMap
     */
    private void addTableMeta(@NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // http 注释标识
        objectMap.put("httpComment", "###");
        // 版本
        objectMap.put("since", OrionOpsProConst.VERSION);
        // 替换业务注释
        tableInfo.setComment(tables.get(tableInfo.getName()).getComment());
        Table table = tables.get(tableInfo.getName());
        // 缓存元数据
        Map<String, Object> cacheMeta = this.pickTableMeta(table,
                "enableCache", "cacheKey", "cacheDesc",
                "cacheExpired", "cacheExpireTime", "cacheExpireUnit");
        objectMap.put("cacheMeta", cacheMeta);
        // 实体名称
        String domainName = tableInfo.getEntityName();
        String mappingHyphen = objectMap.get("controllerMappingHyphen").toString();
        String entityName = domainName.substring(0, domainName.length() - 2);
        // 类型
        objectMap.put("type", entityName);
        // 类型首字母小写
        objectMap.put("typeLower", Strings.firstLower(entityName));
        // 类型脊柱名称
        objectMap.put("typeHyphen", mappingHyphen.substring(0, mappingHyphen.length() - 3));
        // 类型常量
        objectMap.put("typeConst", VariableStyles.BIG_HUMP.toSerpentine(entityName).toUpperCase());
    }

    /**
     * 替换自定义文件包
     *
     * @param customFiles 自定义文件
     * @param tableInfo   tableInfo
     * @param objectMap   objectMap
     */
    private void replacePackageName(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 替换包名
        customFiles.forEach(s -> {
            // 反射调用 setter 方法
            BiConsumer<String, Object> callSetter = (field, value) -> Fields.setFieldValue(s, field, value);
            String packageName = s.getPackageName();
            // 替换文件业务包名
            if (packageName.contains(Const.DOLLAR)) {
                Map<String, Object> meta = new HashMap<>(4);
                meta.put("bizPackage", tables.get(tableInfo.getName()).getBizPackage());
                // 调用 setter
                callSetter.accept("packageName", Strings.format(packageName, meta));
            }
        });

        // 包转换器
        Function<Predicate<String>, List<String>> packageConverter = conv ->
                customFiles.stream()
                        .filter(s -> conv.test(s.getTemplatePath()))
                        .map(CustomFile::getPackageName)
                        .map(s -> getConfigBuilder().getPackageConfig().getParent() + "." + s)
                        .distinct()
                        .collect(Collectors.toList());

        // 自定义 module 文件的包 (导入用)
        List<String> customModuleFilePackages = packageConverter.apply(s -> s.contains(".java.vm") && s.contains("orion-server-module"));
        objectMap.put("customModuleFilePackages", customModuleFilePackages);

        // 自定义 provider entity 文件的包 (导入用)
        List<String> customProviderEntityFilePackages = packageConverter.apply(s -> s.contains(".java.vm") && s.contains("orion-server-provider-entity"));
        objectMap.put("customProviderEntityFilePackages", customProviderEntityFilePackages);

        // 自定义 provider interface 文件的包 (导入用)
        List<String> customProviderFilePackages = packageConverter.apply(s -> s.contains(".java.vm") && s.contains("orion-server-provider"));
        objectMap.put("customProviderFilePackages", customProviderFilePackages);
    }

    /**
     * 插入 api 注释
     *
     * @param tableInfo tableInfo
     * @param objectMap objectMap
     */
    private void addApiCommentMeta(@NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        Map<String, String> map = new HashMap<>(12);
        objectMap.put("apiComment", map);
        String comment = tableInfo.getComment();
        map.put("create", "创建" + comment);
        map.put("updateAll", "根据条件更新" + comment);
        map.put("updateById", "更新" + comment);
        map.put("getById", "查询" + comment);
        map.put("getByIdList", "批量查询" + comment);
        map.put("queryList", "查询全部" + comment);
        map.put("queryListByCache", "通过缓存查询" + comment);
        map.put("queryPage", "分页查询" + comment);
        map.put("queryCount", "查询" + comment + "数量");
        map.put("deleteById", "删除" + comment);
        map.put("deleteAll", "根据条件删除" + comment);
        map.put("batchDelete", "批量删除" + comment);
        map.put("export", "导出" + comment);
    }

    /**
     * 生成后端文件
     *
     * @param customFiles customFiles
     * @param tableInfo   tableInfo
     * @param objectMap   objectMap
     */
    private void generatorServerFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 过滤文件
        customFiles = customFiles.stream()
                .filter(s -> this.isServerFile(s.getTemplatePath()))
                .collect(Collectors.toList());

        // 生成文件
        customFiles.forEach(file -> {
            // 获取 parent package
            String currentPackage = getConfigBuilder().getPackageConfig().getParent() + "." + file.getPackageName();
            // 设置当前包
            objectMap.put("currentPackage", currentPackage);

            // 文件路径
            String filePath = this.getPathInfo(OutputFile.parent) + File.separator + file.getPackageName()
                    .replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
            // 文件名称
            Map<String, Object> fileNameMeta = new HashMap<>(4);
            fileNameMeta.put("type", objectMap.get("type"));
            fileNameMeta.put("tableName", tableInfo.getName());
            String fileName = filePath + File.separator + Strings.format(file.getFileName(), fileNameMeta);
            // 渲染文件
            this.outputFile(Files1.newFile(fileName), objectMap, file.getTemplatePath(), file.isFileOverride());
        });
    }

    /**
     * 生成前端文件
     *
     * @param customFiles customFiles
     * @param tableInfo   tableInfo
     * @param objectMap   objectMap
     */
    private void generatorVueFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 不生成 vue 文件
        if (!tables.get(tableInfo.getName()).isEnableVue()) {
            return;
        }
        // 过滤文件
        customFiles = customFiles.stream()
                .filter(s -> this.isVueFile(s.getTemplatePath()))
                .collect(Collectors.toList());
        // 设置前端元数据
        Table table = tables.get(tableInfo.getName());
        Map<String, Object> vueMeta = this.pickTableMeta(table, "module", "feature", "enableDrawerForm", "enableRowSelection", "enableCardView");
        // 模块名称实体
        vueMeta.put("moduleEntity", VariableStyles.SPINE.toBigHump(table.getModule()));
        // 模块名称实体
        vueMeta.put("moduleEntityFirstLower", Strings.firstLower(vueMeta.get("moduleEntity")));
        // 模块名称常量
        vueMeta.put("moduleConst", VariableStyles.SPINE.toSerpentine(table.getModule()).toUpperCase());
        // 功能名称实体
        vueMeta.put("featureEntity", VariableStyles.SPINE.toBigHump(table.getFeature()));
        // 功能名称实体
        vueMeta.put("featureEntityFirstLower", Strings.firstLower(vueMeta.get("featureEntity")));
        // 功能名称常量
        vueMeta.put("featureConst", VariableStyles.SPINE.toSerpentine(table.getFeature()).toUpperCase());
        // 枚举
        vueMeta.put("enums", this.getEnumMap(tableInfo, table));
        objectMap.put("vue", vueMeta);

        // 生成文件
        customFiles.forEach(file -> {
            // 文件路径
            String filePath = getConfigBuilder().getGlobalConfig().getOutputDir()
                    + "/" + Strings.format(file.getPackageName(), vueMeta)
                    + "/" + Strings.format(file.getFileName(), vueMeta);
            // 渲染文件
            this.outputFile(Files1.newFile(filePath), objectMap, file.getTemplatePath(), file.isFileOverride());
        });
    }

    /**
     * 是否为后端文件
     *
     * @param templatePath templatePath
     * @return 是否为后端文件
     */
    private boolean isServerFile(String templatePath) {
        return templatePath.contains("orion-server");
    }

    /**
     * 是否为后端 provider 文件
     *
     * @param templatePath templatePath
     * @return 是否为后端 provider 文件
     */
    private boolean isServerProviderFile(String templatePath) {
        return templatePath.contains("orion-server-provider");
    }

    /**
     * 是否为后端 provider 单元测试文件
     *
     * @param templatePath templatePath
     * @return 是否为后端 provider 单元测试文件
     */
    private boolean isServerProviderTestFile(String templatePath) {
        return templatePath.contains("orion-server-test-api");
    }

    /**
     * 是否为后端单元测试文件
     *
     * @param templatePath templatePath
     * @return 是否为后端单元测试文件
     */
    private boolean isServerUnitTestFile(String templatePath) {
        return templatePath.contains("orion-server-test");
    }

    /**
     * 是否为后端缓存文件
     *
     * @param templatePath templatePath
     * @return 是否为后端缓存文件
     */
    private boolean isServerCacheFile(String templatePath) {
        return templatePath.contains("orion-server-module-cache");
    }

    /**
     * 是否为 vue 文件
     *
     * @param templatePath templatePath
     * @return 是否为 vue 文件
     */
    private boolean isVueFile(String templatePath) {
        return templatePath.contains("orion-vue-") ||
                templatePath.contains("orion-sql-menu.sql");
    }

    /**
     * 获取表元数据
     *
     * @param table table
     * @param keys  keys
     * @return meta
     */
    private Map<String, Object> pickTableMeta(Table table, String... keys) {
        BeanMap beanMap = BeanMap.create(table);
        Map<String, Object> tableMeta = new HashMap<>();
        for (String key : keys) {
            tableMeta.put(key, beanMap.get(key));
        }
        return tableMeta;
    }

    /**
     * 获取枚举
     *
     * @param tableInfo tableInfo
     * @param table     table
     * @return enumMap
     */
    private Map<String, EnumMeta> getEnumMap(TableInfo tableInfo, Table table) {
        // 枚举值
        Map<String, EnumMeta> enumMap = new LinkedHashMap<>();
        for (VueEnum meta : table.getEnums()) {
            // 检查字段是否存在
            String variable = meta.getVariable();
            TableField tableField = tableInfo.getFields()
                    .stream()
                    .filter(s -> variable.equals(s.getName()) || variable.equals(s.getPropertyName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("未查询到枚举映射字段 " + variable));
            // 设置枚举名称
            if (meta.getClassName() == null) {
                meta.setClassName(Strings.firstUpper(tableField.getPropertyName()) + "Enum");
            }
            // 设置枚举注释
            if (meta.getComment() == null) {
                meta.setComment(Strings.def(tableField.getComment(), meta.getClassName()));
            }
            // 设置枚举
            MultiLinkedHashMap<String, String, Object> enumInfo = new MultiLinkedHashMap<>();
            for (int i = 0; i < meta.getNames().size(); i++) {
                // 枚举名称
                String name = meta.getNames().get(i);
                // 设置枚举值
                for (int j = 0; j < meta.getFields().size(); j++) {
                    String field = meta.getFields().get(j);
                    Object value = safeGet(safeGet(meta.getValues(), j), i);
                    enumInfo.put(name, field, value);
                }
                // 检查是否有 value
                if (!meta.getFields().contains("value")) {
                    // 没有 value 用 name
                    enumInfo.put(name, "value", name);
                }
            }
            enumMap.put(tableField.getPropertyName(), new EnumMeta(meta.getClassName(), meta.getComment(), enumInfo));
        }
        return enumMap;
    }

    /**
     * 获取值
     *
     * @param list  list
     * @param index index
     * @param <T>   T
     * @return value
     */
    private <T> T safeGet(List<T> list, int index) {
        if (list == null) {
            return null;
        }
        if (list.size() > index) {
            return list.get(index);
        } else {
            return null;
        }

    }

}
