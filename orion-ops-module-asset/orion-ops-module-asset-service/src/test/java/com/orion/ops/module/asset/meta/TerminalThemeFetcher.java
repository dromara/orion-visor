package com.orion.ops.module.asset.meta;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.orion.lang.utils.Colors;
import com.orion.lang.utils.awt.Clipboards;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.FileReaders;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.reflect.Fields;
import lombok.Data;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 终端主题拉取 __META__
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/7 10:52
 */
public class TerminalThemeFetcher {

    public static void main(String[] args) {
        List<File> files = Files1.listFiles("D:\\idea-project\\iTerm2-Color-Schemes\\vhs");
        // 过滤的 theme
        List<String> schemaFilter = new ArrayList<>();
        // List<String> schemaFilter = Lists.of("oneHalf", "material", "github", "iTerm2", "JetBrains");
        // List<String> schemaFilter = Lists.of("catppuccin", "3024", "OneHalfDark", "OneHalfLight", "MaterialDesignColors", "MaterialOcean", "Solarized Light");
        // 颜色大写
        ValueFilter colorFilter = (Object object, String name, Object value) -> {
            if (value instanceof String && value.toString().contains("#")) {
                return ((String) value).toUpperCase();
            } else {
                return value;
            }
        };
        // 转换
        List<TerminalTheme> arr = files.stream()
                .filter(f -> Lists.isEmpty(schemaFilter) || schemaFilter.stream()
                        .anyMatch(s -> f.getName().toLowerCase().contains(s.toLowerCase())))
                .limit(200)
                .map(f -> {
                    JSONObject schema = JSONObject.parseObject(new String(FileReaders.readAllBytes(f)));
                    schema.put("dark", isDarkColor(schema.getString("background")));
                    schema.put("selectionBackground", schema.getString("selection"));
                    // 转为对象
                    return JSON.parseObject(JSON.toJSONString(schema, colorFilter), TerminalTheme.class);
                }).collect(Collectors.toList());

        // 打印 json
        String json = JSON.toJSONString(arr, colorFilter);
        System.out.println("\n\n" + json);
        // 转为 jsCode
        List<String> formatter = Fields.getFields(TerminalTheme.class)
                .stream()
                .map(Field::getName)
                .collect(Collectors.toList());
        for (String s : formatter) {
            json = json.replaceAll("\"" + s + "\"", s);
        }
        Clipboards.setString(json);
        System.out.println("\n\njsCode 已复制到剪切板");
    }

    /**
     * TODO kit
     * 是否为深色
     * 亮度值 < 128 ? 深色 : 128
     *
     * @param hex hex
     * @return 是否为深色
     */
    public static boolean isDarkColor(String hex) {
        int[] rgb = Colors.toRgbColor(hex);
        return (rgb[0] * 299 + rgb[1] * 587 + rgb[2] * 114) / 1000 < 128;
    }

    //
    /*
      var term = new Terminal();
      var doc = document.getElementById('themes');
      for (let t of themes) {
        var span = document.createElement('span');
        span.innerHTML = t.name;
        span.style.display = 'inline-block';
        span.style.padding = '4px 8px';
        span.style.margin = '4px';
        span.style.border = '1px solid green';
        if(t.dark) {
          span.style.background = '#000';
          span.style.color = '#FFF';
        }
        span.addEventListener('click', function() {
          term.setOption('theme', t);
          term.reset();
          for (let i = 0; i < 9; i++) {
            term.write('[' + (30 + i) + 'm');
            term.write('          ' + i + 'orion ops pro');
            term.write('[0m\r\n');
          }
          for (let i = 0; i < 9; i++) {
            term.write('[' + (90 + i) + 'm');
            term.write('          ' + i + 'orion ops pro');
            term.write('[0m\r\n');
          }
        });
        doc.append(span);
      }
      term.open(document.getElementById('terminal'));
     */

    @Data
    public static class TerminalTheme {
        @JSONField(ordinal = 0)
        private String name;
        @JSONField(ordinal = 1)
        private Boolean dark;
        @JSONField(ordinal = 2)
        private String background;
        @JSONField(ordinal = 3)
        private String foreground;
        @JSONField(ordinal = 4)
        private String cursor;
        @JSONField(ordinal = 5)
        private String selectionBackground;
        @JSONField(ordinal = 6)
        private String black;
        @JSONField(ordinal = 7)
        private String red;
        @JSONField(ordinal = 8)
        private String green;
        @JSONField(ordinal = 9)
        private String yellow;
        @JSONField(ordinal = 10)
        private String blue;
        @JSONField(ordinal = 11)
        private String magenta;
        @JSONField(ordinal = 12)
        private String cyan;
        @JSONField(ordinal = 13)
        private String white;
        @JSONField(ordinal = 14)
        private String brightBlack;
        @JSONField(ordinal = 15)
        private String brightRed;
        @JSONField(ordinal = 16)
        private String brightGreen;
        @JSONField(ordinal = 17)
        private String brightYellow;
        @JSONField(ordinal = 18)
        private String brightBlue;
        @JSONField(ordinal = 19)
        private String brightMagenta;
        @JSONField(ordinal = 20)
        private String brightCyan;
        @JSONField(ordinal = 21)
        private String brightWhite;
    }

}
