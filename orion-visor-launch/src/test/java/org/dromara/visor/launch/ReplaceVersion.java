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

import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.FileWriters;
import cn.orionsec.kit.lang.utils.io.Files1;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * 替换版本号
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 10:21
 */
public class ReplaceVersion {

    private static final String TARGET_VERSION = "2.5.3";

    private static final String REPLACE_VERSION = "2.5.4";

    private static final String PATH = new File("").getAbsolutePath();

    private static final String[] DOCKER_FILES = new String[]{
            "docker/docker-build.sh",
            "docker/project-build.sh",
            "docker-compose.yaml",
            "docker-compose-testing.yaml"
    };

    private static final String[] POM_FILES = new String[]{
            "pom.xml",
            "orion-visor-dependencies/pom.xml"
    };

    private static final String APP_CONST_FILE = "orion-visor-common/src/main/java/org/dromara/visor/common/constant/AppConst.java";

    private static final String PACKAGE_JSON_FILE = "orion-visor-ui/package.json";

    private static final String[] VITE_ENV_FILES = new String[]{
            "orion-visor-ui/.env.development",
            "orion-visor-ui/.env.production"
    };

    public static void main(String[] args) {
        replaceDockerFiles();
        replacePomFiles();
        replaceAppConst();
        replacePackageJson();
        replaceViteEnvFiles();
    }

    /**
     * 替换 docker 文件
     */
    private static void replaceDockerFiles() {
        for (String file : DOCKER_FILES) {
            readAndWrite(file, s -> s.replaceAll(TARGET_VERSION, REPLACE_VERSION));
        }
    }

    /**
     * 替换 pom 文件
     */
    private static void replacePomFiles() {
        for (String file : POM_FILES) {
            readAndWrite(file, s -> s.replaceAll("<revision>" + TARGET_VERSION + "</revision>", "<revision>" + REPLACE_VERSION + "</revision>"));
        }
    }

    /**
     * 替换 AppConst 文件
     */
    private static void replaceAppConst() {
        readAndWrite(APP_CONST_FILE, s -> s.replaceAll("String VERSION = \"" + TARGET_VERSION + "\"", "String VERSION = \"" + REPLACE_VERSION + "\""));
    }

    /**
     * 替换 package.json 文件
     */
    private static void replacePackageJson() {
        readAndWrite(PACKAGE_JSON_FILE, s -> s.replaceAll("\"version\": \"" + TARGET_VERSION + "\"", "\"version\": \"" + REPLACE_VERSION + "\""));
    }

    /**
     * 替换 .env 文件
     */
    private static void replaceViteEnvFiles() {
        for (String file : VITE_ENV_FILES) {
            readAndWrite(file, s -> s.replaceAll("VITE_APP_VERSION=" + TARGET_VERSION, "VITE_APP_VERSION=" + REPLACE_VERSION));
        }
    }

    /**
     * 读取并且写入
     *
     * @param path    path
     * @param mapping mapping
     */
    private static void readAndWrite(String path, Function<String, String> mapping) {
        String filePath = Files1.getPath(PATH, path);
        try {
            // 读取文件内容
            byte[] bytes = FileReaders.readAllBytesFast(filePath);
            // 写入文件内容
            FileWriters.writeFast(filePath, mapping.apply(new String(bytes)).getBytes(StandardCharsets.UTF_8));
            System.out.println("OK:  " + path);
        } catch (Exception e) {
            System.err.println("ERR: " + path);
        }
    }

}
