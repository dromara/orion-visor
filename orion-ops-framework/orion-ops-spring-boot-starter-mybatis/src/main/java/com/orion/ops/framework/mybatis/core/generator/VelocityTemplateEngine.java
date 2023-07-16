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
package com.orion.ops.framework.mybatis.core.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.orion.lang.utils.Strings;
import com.orion.ops.framework.common.constant.Const;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 代码生成器 Velocity 引擎
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/4/20 10:33
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine {

    private final Map<String, String> tableComment;

    private VelocityEngine velocityEngine;

    public VelocityTemplateEngine(Map<String, String> tableComment) {
        this.tableComment = tableComment;
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
            velocityEngine = new VelocityEngine(p);
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
        LOGGER.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }

    @Override
    @NotNull
    public String templateFilePath(@NotNull String filePath) {
        final String dotVm = ".vm";
        return filePath.endsWith(dotVm) ? filePath : filePath + dotVm;
    }

    /**
     * 插入 api 注释
     *
     * @param tableInfo tableInfo
     * @param objectMap objectMap
     */
    private void putApiComment(TableInfo tableInfo, Map<String, Object> objectMap) {
        Map<String, String> map = new HashMap<>();
        objectMap.put("apiComment", map);
        String comment = tableInfo.getComment();
        map.put("create", "创建" + comment);
        map.put("update", "通过 id 更新" + comment);
        map.put("get", "通过 id 查询" + comment);
        map.put("list", "通过 id 批量查询" + comment);
        map.put("query", "分页查询" + comment);
        map.put("delete", "通过 id 删除" + comment);
        map.put("batchDelete", "通过 id 批量删除" + comment);
    }

    /**
     * 输出自定义模板文件
     *
     * @param customFiles 自定义模板文件列表
     * @param tableInfo   表信息
     * @param objectMap   渲染数据
     * @since 3.5.3
     */
    @Override
    protected void outputCustomFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        // 替换业务注释
        String comment = tableComment.get(tableInfo.getName());
        if (comment != null) {
            tableInfo.setComment(comment);
        }

        // http 注释标识
        objectMap.put("httpComment", "###");
        // 版本
        objectMap.put("since", Const.VERSION);

        // 实体名称
        String domainName = tableInfo.getEntityName();
        String mappingHyphen = objectMap.get("controllerMappingHyphen").toString();
        String entityName = domainName.substring(0, domainName.length() - 2);
        objectMap.put("type", entityName);
        objectMap.put("typeLower", Strings.firstLower(entityName));
        objectMap.put("typeHyphen", mappingHyphen.substring(0, mappingHyphen.length() - 3));
        // 注释
        this.putApiComment(tableInfo, objectMap);

        // 自定义文件的包
        List<String> customFilePackages = customFiles.stream()
                .filter(s -> s.getTemplatePath().contains(".java.vm"))
                .map(CustomFile::getPackageName)
                .map(s -> getConfigBuilder().getPackageConfig().getParent() + "." + s)
                .distinct()
                .collect(Collectors.toList());
        // 设置导入的包
        objectMap.put("customFilePackages", customFilePackages);

        // 生成文件
        String parentPath = getPathInfo(OutputFile.parent);
        customFiles.forEach(file -> {
            // 获取 parent package
            String currentPackage = getConfigBuilder().getPackageConfig().getParent() + "." + file.getPackageName();
            // 设置当前包
            objectMap.put("currentPackage", currentPackage);

            // 文件路径
            String filePath = StringUtils.isNotBlank(file.getFilePath()) ? file.getFilePath() : parentPath;
            if (StringUtils.isNotBlank(file.getPackageName())) {
                filePath = filePath + File.separator + file.getPackageName();
                filePath = filePath.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
            }
            String fileName = filePath + File.separator + String.format(file.getFileName(), entityName);
            outputFile(new File(fileName), objectMap, file.getTemplatePath(), file.isFileOverride());
        });
    }

}
