package com.xwbing.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUrlImageUtil {
    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        String url = "http://oss.drore.com/material/6fad1079fc794de6a6c309a1bfba685a/201707/17/5ae7640a7b8b485eb866094b3d059519.jpg";
        String pathName="C:/Users/10232/Desktop";
        byte[] btImg = getImageFromNetByUrl(url);
        if(null != btImg && btImg.length > 0){
            System.out.println("读取到：" + btImg.length + " 字节");
            String fileName = "百度.gif";
            writeImageToDisk(btImg, pathName,fileName);
        }else{
            System.out.println("没有从该连接获得内容");
        }
    }
    /**
     * 将图片写入到磁盘
     * @param img 图片数据流
     * @param fileName 文件保存时的名称
     */
    public static void writeImageToDisk(byte[] img,String pathName,String fileName){
        try {
            File file = new File(pathName+"/"+fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(img);
            fos.close();
            System.out.println("图片已经写入到磁盘");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    private static byte[] getImageFromNetByUrl(String strUrl){
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            return readInputStream(inStream);//得到图片的二进制数据
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    private static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        while( (len=inStream.read(data)) != -1 ){
            outStream.write(data, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
