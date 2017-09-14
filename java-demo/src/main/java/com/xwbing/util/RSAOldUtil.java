package com.xwbing.util;  
import java.security.*;  
import java.security.interfaces.*;  
import java.io.*;  
import java.math.*;  
import java.net.URLDecoder;
import java.net.URLEncoder;
/**
 * 
 * 说明:
 * RSA的算法中有两个钥匙，一个公钥一个私钥。
 * 用公钥加密的数据，只有私钥能解开；
 * RSA的算法涉及三个参数，n、e1、e2。
 * e1和e2是一对相关的值,可以由相应钥匙获取。n为模数
 * （n，e1),(n，e2)就是密钥对。其中(n，e1)为公钥，(n，e2)为私钥。
 * m为明文，c为密文,可以相互转换。
 * c=m.modPow(e1, n);m = c.modPow(e2, n)。
 * 
 * 创建日期: 2016年10月28日 上午9:47:02
 * 作者: xwb
 */
@Deprecated
public class RSAOldUtil {  
    public RSAOldUtil() {  
    }  
    /**
     * 
     * 功能描述：     生成密匙对                  <br/>
     * 作    者：xwb <br/>
     * 创建时间：2016年10月28日  上午9:48:35 <br/>
     */
    public static void createKey() {  
        try {  
        	//返回生成指定算法的密钥对的 KeyPairGenerator对象
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            //指定密钥长度
            kpg.initialize(1024);  
            //生成密钥对
            KeyPair kp = kpg.genKeyPair(); 
            //生成公钥
            PublicKey pbkey = kp.getPublic();
            //生成私钥
            PrivateKey prkey = kp.getPrivate(); 
            // 保存公钥
            FileOutputStream f1 = new FileOutputStream("pubkey.dat");  
            ObjectOutputStream b1 = new ObjectOutputStream(f1);  
            b1.writeObject(pbkey); 
            b1.close();
            // 保存私钥
            FileOutputStream f2 = new FileOutputStream("privatekey.dat");  
            ObjectOutputStream b2 = new ObjectOutputStream(f2);  
            b2.writeObject(prkey);  
            b2.close();
        } catch (Exception e) {  
        }  
    }  
   /**
    * 
    * 功能描述：       加密                 <br/>
    * 作    者：xwb <br/>
    * 创建时间：2016年10月28日  上午8:57:09 <br/>
    * @param code
    * @return
    * @throws Exception
    */
    public static BigInteger encrypt(String code) throws Exception {  
    	// 获取公钥 
        FileInputStream f = new FileInputStream("pubkey.dat");  
        ObjectInputStream b = new ObjectInputStream(f);  
        RSAPublicKey pbk = (RSAPublicKey) b.readObject(); //获取公钥
        b.close();
        //获取参数e1和n
        BigInteger e1 = pbk.getPublicExponent(); 
        BigInteger n = pbk.getModulus();  
        System.out.println("e1= " + e1);  
        System.out.println("n= " + n);  
        // 加密
        code=URLEncoder.encode(code, "UTF-8");
        byte mtext[] = code.getBytes("UTF-8");  
        BigInteger m = new BigInteger(mtext);  
        BigInteger c = m.modPow(e1, n);
        return c; 
    }  
   /**
    * 
    * 功能描述：        解密                <br/>
    * 作    者：xwb <br/>
    * 创建时间：2016年10月28日  上午8:59:01 <br/>
    * @param cipher
    * @return
    * @throws Exception
    */
    public static String decrypt(BigInteger cipher) throws Exception {  
        // 读取私钥
        FileInputStream f = new FileInputStream("privatekey.dat");  
        ObjectInputStream b = new ObjectInputStream(f);  
        RSAPrivateKey prk = (RSAPrivateKey) b.readObject();  
        b.close();
        // 获取私钥参数及解密  
        BigInteger e2 = prk.getPrivateExponent(); //获取参数e2
        BigInteger n = prk.getModulus(); //获取模数n
        System.out.println("e2= " + e2);  
        System.out.println("n= " + n); 
        BigInteger m = cipher.modPow(e2, n); //获取密文
        byte[] mtext = m.toByteArray();
        String result=new String(mtext,"UTF-8");
        result=URLDecoder.decode(result, "UTF-8");
        return result;       
    }  
    public static void main(String args[]) {  
        try {  
        	createKey();
        	String code="hello word!";
        	BigInteger c=encrypt(code);
        	String result=decrypt(c);
        	System.out.println("解密结果："+result);
  
        } catch (Exception e) {  
            System.out.println(e.toString());  
        }  
    }  
}  
