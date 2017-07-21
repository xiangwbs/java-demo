package com.xwbing.demo;

import java.io.*;

/**
 * 说明: <br/>
 * 项目名称: zdemo <br/>
 * 创建日期: 2017年2月20日 上午10:12:20 <br/>
 * 作者: xwb
 */

public class IODemo {
    public static void main(String[] args) throws Exception {
        /**
         * GBK：国标编码，中文占2字节
         * UTF-8：unicode的子集，其中文占3个字节
         * ISO8859-1：欧洲编码集，不支持中文
         */
        
        /**
         * 写
         */
        FileOutputStream  fos=new FileOutputStream ("fos.txt",true);//追加写模式
        fos.write("你好".getBytes("gbk"));
        System.out.println("写出完毕");
        fos.close();//关闭流
        
        /**
         * 读
         */
        FileInputStream fis=new FileInputStream("fos.txt");
        byte[] data=new byte[1024*10];
        int len=fis.read(data);
        String str=new String(data,0,len,"gbk");
        fis.close();
        InputStream is = new ByteArrayInputStream(str.getBytes("gbk"));
        byte[] da = new byte[is.available()];
        is.read(da);
        String s=new String(da,0,len,"gbk");
        is.close();
        /**
         * 缓冲流
         *
         */
        BufferedOutputStream bos=new BufferedOutputStream(fos);
        bos.write("你好".getBytes());
        bos.close();//close前会自动flush，关流只需管最外层高级流
        
        /**
         * 使用缓冲流的形式复制文件
         */
        BufferedInputStream bis1=new BufferedInputStream(fis);
        BufferedOutputStream bos1=new BufferedOutputStream(fos);
        int d=-1;
        while((d=bis1.read())!=-1){
            bos1.write(d);
        }
        bis1.close();
        bos1.close();
        
        /*
         * 字符流
         * 读写单位是字符
         * 可以按照指定的字符集，将写出的字符转换为对应的字节
         * 字符流只适合读写文本数据
         * 转码
         *uft-8转gbk
         */
        InputStreamReader isr=new InputStreamReader(fis,"utf-8");
        OutputStreamWriter osw=new OutputStreamWriter(fos,"gbk");
        int leng=1;
        while((leng=isr.read())!=-1){
            osw.write(leng);
        }
        osw.close();
        isr.close();
        
        /**
         * 缓冲字符输入流，特点：读取速度快，并且可以按行读取字符串
         */
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("fis.txt")));
        String line =null;
        //readerLIne():连续读取若干字符，知道遇到换行符为止,该字符串中不包含换行符
        while((line=br.readLine())!=null){
            //TODO
        }
        br.close();
        
        /**
         * 缓冲字符输出流
         */
        PrintWriter pw= new PrintWriter("e.txt","utf-8");//PrintWriter可以直接创建基于文件进行写操作
        
        FileOutputStream fos1=new FileOutputStream("pw1.txt");
        OutputStreamWriter osw1=new OutputStreamWriter(fos,"utf-8");//可以指定字符
        PrintWriter pw1=new PrintWriter(osw,true);///////当第一个参数为流，可以使用第二个参数来指定是否自动flush
        pw1.println("......");
        pw1.close();
        
        
        /**
         * 读取配置文件的2种方式
         */
        String filePath=IODemo.class.getResource("/conf/jdbc.properties").getPath();
        InputStream in= IODemo.class.getResourceAsStream("/conf/jdbc.properties");
    }
}
