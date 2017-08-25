package com.xwbing.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明: 文件操作工具类
 * 创建日期: 2017年3月3日 下午5:17:14
 * 作者: xwb
 */
public class FileUtils {
//    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 文件转换byte字节数组
     *
     * @param file
     * @return
     */
    public static byte[] toByte(File file) {
        {
            byte[] bytes = null;
            try {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] data = new byte[1024 * 10];
                int len;
                while ((len = fis.read(data)) != -1) {
                    bos.write(data, 0, len);
                }
                bytes = bos.toByteArray();
                fis.close();
                bos.close();
            } catch (IOException e) {
//                logger.error("" + e);
            }
            return bytes;
        }
    }

    public static byte[] toByte(String filePath) {
        return toByte(new File(filePath));
    }

    /**
     * 获取文件后缀 abc.jpg reutrn jpg
     *
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        String fileName = file.getName();
        return StringUtils.lowerCase(StringUtils.substring(fileName,
                StringUtils.lastIndexOf(fileName, ".") + 1));

    }

    public static String getFileSuffix(String filePath) {
        return getFileSuffix(new File(filePath));
    }


    /**
     * 逐行读取文件的内容,并存入list集合中
     *
     * @param fileName
     * @return
     */
    public static List<String> readLines(String fileName) {
        InputStream in = null;
        BufferedReader br = null;
        List<String> list = new ArrayList<String>();
        String line = null;
        try {
            in = FileUtils.class.getResourceAsStream(fileName);
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(in);
        }
        return list;
    }

    /**
     * 读取文件转为字符串
     *
     * @param fileName
     * @return
     */
    public static String readLinesToString(String fileName) {
        InputStream in = null;
        BufferedReader br = null;
        StringBuffer str = new StringBuffer();
        String line = null;
        try {
            in = FileUtils.class.getResourceAsStream(fileName);
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((line = br.readLine()) != null) {
                str.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(in);
        }
        return str.toString();
    }
}
