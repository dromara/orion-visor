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
package com.orion.visor.launch;

import com.orion.lang.define.StopWatch;
import com.orion.lang.utils.io.FileReaders;
import com.orion.lang.utils.io.FileWriters;
import com.orion.lang.utils.io.Files1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/11 13:52
 */
public class AddLicenseHeader {

    private static final String LICENSE = "/*\n" +
            " * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).\n" +
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
                && !file.getAbsolutePath().contains("node_modules"), true, false);
        sw.tag("list");
        // 添加头
        files.forEach(AddLicenseHeader::addLicenseToFile);
        sw.tag(" add");
        sw.stop();
        System.out.println(sw);
    }

    /**
     * 添加 license
     *
     * @param file file
     */
    private static void addLicenseToFile(File file) {
        try {
            String line = FileReaders.readLine(file);
            if (line != null && line.trim().equals("/*")) {
                System.out.println("License already exists in " + file.getAbsolutePath());
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
            System.out.println("License added to " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to add license to " + file.getAbsolutePath());
        }
    }

}
