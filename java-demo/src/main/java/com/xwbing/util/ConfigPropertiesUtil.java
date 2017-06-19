package com.xwbing.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * 说明:读取properties配置文件 <br/>
 * 创建日期: 2017年3月3日 上午9:25:27 <br/>
 * 作者: xwb
 */
public class ConfigPropertiesUtil {
    public static final String xxx = "xxx";

    // 根据Key读取Value
    public static String getValueByKey(String key) {
        Properties pps = new Properties();
        // 获取文件路径
        String filePath = ConfigPropertiesUtil.class.getClassLoader()
                .getResource("default.properties").getPath();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(
                    filePath));
            pps.load(in);
            String value = pps.getProperty(key.trim());
            in.close();
            return value;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }

}
