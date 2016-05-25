package com.amitgoenka.encryption.props;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class to handle Yaml property files
 */
class YamlUtil {

    static Map<?, ?> loadYaml(String fileName) throws FileNotFoundException {
        return StringUtils.hasLength(fileName) && new FileSystemResource(fileName).getFile().exists()
                ? (LinkedHashMap<?, ?>) new Yaml().load(new FileInputStream(new FileSystemResource(fileName).getFile()))
                : null;
    }

    static void dumpYaml(String fileName, Map<?, ?> map) throws IOException {
        if (StringUtils.hasLength(fileName)) {
            FileWriter fileWriter = new FileWriter(new FileSystemResource(fileName).getFile());
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(map, fileWriter);
        }
    }

    static Object getProperty(Map<?, ?> map, Object qualifiedKey) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!input.equals("")) {
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1, input.length());
                    return getProperty((Map<?, ?>) map.get(left), right);
                } else if (map.containsKey(input)) return map.get(input);
                else return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static void setProperty(Map<?, ?> map, Object qualifiedKey, Object value) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!input.equals("")) {
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1, input.length());
                    setProperty((Map<?, ?>) map.get(left), right, value);
                } else ((Map<Object, Object>) map).put(qualifiedKey, value);
            }
        }
    }

}