package com.yws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private Properties properties;

    public Config(String fileName) throws IOException {
        properties = new Properties();
        //FileInputStream fileInputStream = new FileInputStream("com/yws/server_config.properties");
        InputStream fileInputStream = Config.class.getClassLoader().getResourceAsStream(fileName);
        properties.load(fileInputStream);
    }

    public String getStringValue(String key) {
        return properties.getProperty(key);
    }

    public int getIntValue(String key) {
        return Integer.parseInt(properties.getProperty(key, "-1"));//不存在，返回默认-1
    }

    public static void main(String[] args) throws IOException {
        Config config = new Config("com/yws/server_config.properties");

        String boss = config.getStringValue("name");
        System.out.println(boss);
    }
}
