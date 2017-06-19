package com.xwbing.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * 
 * 说明: 文件操作工具类 <br/>
 * 创建日期: 2017年3月3日 下午5:17:14 <br/>
 * 作者: xwb
 */
public class FileUtils {
//    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

  /**
   * 
   * 功能描述：   文件转换byte字节数组                     <br/>
   * 作    者：xwb <br/>
   * 创建时间：2017年3月3日  下午12:57:15 <br/>
   * @param file
   * @return
   */
    public static byte[] toByte(File file) {
        {
            byte[] buffer = null;
            try {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024 * 1024];
                int len;
                while ((len = fis.read(b)) != -1) {
                    bos.write(b, 0, len);
                }
                fis.close();
                bos.close();
                buffer = bos.toByteArray();
            } catch (FileNotFoundException e) {
//                logger.error("" + e);
            } catch (IOException e) {
//                logger.error("" + e);
            }
            return buffer;
        }
    }
    public static byte[] toByte(String filePath) {
        return toByte(new File(filePath));
    }

   /**
    * 
    * 功能描述： 获取文件后缀 abc.jpg reutrn jpg                       <br/>
    * 作    者：xwb <br/>
    * 创建时间：2017年3月3日  下午12:56:38 <br/>
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
    * 
    * 功能描述：    逐行读取文件的内容,并存入list集合中                    <br/>
    * 作    者：xwb <br/>
    * 创建时间：2017年3月3日  下午12:55:12 <br/>
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
    * 
    * 功能描述：  读取文件转为字符串                      <br/>
    * 作    者：xwb <br/>
    * 创建时间：2017年3月3日  下午12:55:58 <br/>
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
            br = new BufferedReader(new InputStreamReader(in, "UTF_8"));
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
