/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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

import cn.orionsec.kit.lang.define.StopWatch;
import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.FileWriters;
import cn.orionsec.kit.lang.utils.io.Files1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 添加 license 头
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/11 13:52
 */
public class AddLicenseHeader {

    private static final String LICENSE = "/*\n" +
            " * Copyright (c) 2023 - present Dromara, All rights reserved.\n" +
            " *\n" +
            " *   https://visor.dromara.org\n" +
            " *   https://visor.dromara.org.cn\n" +
            " *   https://visor.orionsec.cn\n" +
            " *\n" +
            " * Members:\n" +
            " *   lijiahangmax ljh1553488six@139.com - author\n" +
            " *\n" +
            " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            " * you may not use this file except in compliance with the License.\n" +
            " * You may obtain a copy of the License at\n" +
            " *\n" +
            " *   http://www.apache.org/licenses/LICENSE-2.0\n" +
            " *\n" +
            " * Unless required by applicable law or agreed to in writing, software\n" +
            " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            " * See the License for the specific language governing permissions and\n" +
            " * limitations under the License.\n" +
            " */";

    private static final String PATH = new File("").getAbsolutePath();

    public static void main(String[] args) {
        StopWatch sw = StopWatch.begin();
        // 扫描文件
        List<File> files = Files1.listFilesFilter(PATH, file -> file.isFile()
                && (file.getName().endsWith(".java") || file.getName().endsWith(".java.vm"))
                && !file.getAbsolutePath().contains("generated-sources")
                && !file.getAbsolutePath().contains("node_modules"), true, false);
        sw.tag("list");
        // 添加头
        files.forEach(AddLicenseHeader::addLicenseToFile);
        sw.tag(" add");
        sw.stop();
        System.out.println();
        System.out.println(sw);
    }

    /**
     * 添加 license
     *
     * @param file file
     */
    private static void addLicenseToFile(File file) {
        String path = file.getAbsolutePath().substring(PATH.length());
        try {
            String line = FileReaders.readLine(file);
            if (line != null && line.trim().equals("/*")) {
                System.out.println("Exists " + path);
                return;
            }
            // 读取原始文件内容
            byte[] bytes = FileReaders.readAllBytesFast(file);
            // 在头部添加许可证
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(LICENSE.getBytes(StandardCharsets.UTF_8));
            out.write('\n');
            out.write(new String(bytes).replaceAll("\r\n", "\n").getBytes(StandardCharsets.UTF_8));
            // 写入
            FileWriters.writeFast(file, out.toByteArray());
            System.out.println("Added  " + path);
        } catch (IOException e) {
            System.err.println("Failed " + path);
        }
    }

}
