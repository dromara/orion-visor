package com.orion.visor.framework.mybatis.core.generator.core;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.VariableStyles;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.reflect.BeanMap;
import com.orion.lang.utils.reflect.Fields;
import com.orion.visor.framework.common.constant.AppConst;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.mybatis.core.generator.template.Table;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 代码生成器引擎
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/30 11:20
 */
public class CodeGeneratorEngine extends VelocityTemplateEngine {

    private final Map<String, Table> tables;

    public CodeGeneratorEngine(Table[] tables) {
        this.tables = Arrays.stream(tables)
                .collect(Collectors.toMap(Table::getTableName, Function.identity()));
    }

    @Override
    protected void outputCustomFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 过滤文件
        customFiles = new CustomFileFilter(tables.get(tableInfo.getName()), customFiles).doFilter();
        // 添加表元数据
        this.addTableMeta(tableInfo, objectMap);
        // 添加注释元数据
        this.addApiCommentMeta(tableInfo, objectMap);
        // 替换自定义包名
        this.replacePackageName(customFiles, tableInfo, objectMap);
        // 生成后端文件
        this.generatorServerFile(customFiles, tableInfo, objectMap);
        // 生成前端文件
        this.generatorVueFile(customFiles, tableInfo, objectMap);
        // 生成 sql 文件
        this.generatorSqlFile(customFiles, tableInfo, objectMap);
    }

    /**
     * 插入表元数据
     *
     * @param tableInfo tableInfo
     * @param objectMap objectMap
     */
    private void addTableMeta(@NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 替换业务注释
        tableInfo.setComment(tables.get(tableInfo.getName()).getComment());
        Table table = tables.get(tableInfo.getName());
        // 元数据
        Map<String, Object> meta = BeanMap.create(table);
        objectMap.put("meta", meta);
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
        // 字典配置
        objectMap.put("dictMap", new DictParser(tableInfo, table).parse());
    }

    /**
     * 插入 api 注释
     *
     * @param tableInfo tableInfo
     * @param objectMap objectMap
     */
    private void addApiCommentMeta(@NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // http 注释标识
        objectMap.put("httpComment", "###");
        // 版本
        objectMap.put("version", AppConst.VERSION);
        // api 注释
        Map<String, String> apiComment = new HashMap<>(12);
        String comment = tableInfo.getComment();
        apiComment.put("create", "创建" + comment);
        apiComment.put("updateAll", "根据条件更新" + comment);
        apiComment.put("updateById", "更新" + comment);
        apiComment.put("getById", "查询" + comment);
        apiComment.put("getByIdList", "批量查询" + comment);
        apiComment.put("queryList", "查询全部" + comment);
        apiComment.put("queryListByCache", "通过缓存查询" + comment);
        apiComment.put("queryPage", "分页查询" + comment);
        apiComment.put("queryCount", "查询" + comment + "数量");
        apiComment.put("deleteById", "删除" + comment);
        apiComment.put("deleteAll", "根据条件删除" + comment);
        apiComment.put("batchDelete", "批量删除" + comment);
        objectMap.put("apiComment", apiComment);
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
     * 生成后端文件
     *
     * @param customFiles customFiles
     * @param tableInfo   tableInfo
     * @param objectMap   objectMap
     */
    private void generatorServerFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 过滤文件
        customFiles = customFiles.stream()
                .filter(s -> CustomFileFilter.isServerFile(s.getTemplatePath()))
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
                .filter(s -> CustomFileFilter.isVueFile(s.getTemplatePath()))
                .collect(Collectors.toList());
        // 设置前端元数据
        Table table = tables.get(tableInfo.getName());
        Map<String, Object> vueMeta = BeanMap.create(table);
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
     * 生成 sql 文件
     *
     * @param customFiles customFiles
     * @param tableInfo   tableInfo
     * @param objectMap   objectMap
     */
    private void generatorSqlFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 过滤文件
        customFiles = customFiles.stream()
                .filter(s -> CustomFileFilter.isDataSqlFile(s.getTemplatePath()))
                .collect(Collectors.toList());
        // 设置元数据
        Table table = tables.get(tableInfo.getName());
        Map<String, Object> meta = BeanMap.create(table);
        // 生成文件
        customFiles.forEach(file -> {
            // 文件路径
            String filePath = getConfigBuilder().getGlobalConfig().getOutputDir()
                    + "/" + Strings.format(file.getPackageName(), meta)
                    + "/" + Strings.format(file.getFileName(), meta);
            // 渲染文件
            this.outputFile(Files1.newFile(filePath), objectMap, file.getTemplatePath(), file.isFileOverride());
        });
    }

}
