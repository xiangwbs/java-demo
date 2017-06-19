package com.xwbing.demo;
import java.util.Scanner;
public class TwoBigNumberAdd {
	/**
	 * 
	 * ����������       ģ�������������                 <br/>
	 * ��    �ߣ�xwb <br/>
	 * ����ʱ�䣺2016��12��9��  ����11:18:00 <br/>
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String add(String s1,String s2){
		String result="";
		//����
 		int len1=s1.length();
		int len2=s2.length();
		//������ֳ���
		int maxlen=len1>len2?len1:len2;
		int minlen=len1<len2?len1:len2;
		String zero="";
		for(int i=0;i<maxlen-minlen;i++){
			zero+="0";
		}
		//С������ǰ��Ӷ�Ӧ����"0"
		if(maxlen==len1){
			s2=zero+s2;	
		}else{
			s1=zero+s1;
		}
		//��λ0|1
		int jinwei=0;
		//ģ�����ִӺ���ǰ��
		for (int i = maxlen-1; i >=0; i--) {
			int s1number=Integer.valueOf(s1.charAt(i)+"");
			int s2number=Integer.valueOf(s2.charAt(i)+"");
			int sum;
			if(s1number+s2number+jinwei<10){
				sum=s1number+s2number+jinwei;
				jinwei=0;
			}else{
				if(i==0){
					sum=s1number+s2number+jinwei;
				}else{
					sum=s1number+s2number+jinwei-10;
				}
				jinwei=1;
			}
			result=String.valueOf(sum)+result;	
		}
		return result;
	}
	public static void main(String[] args){
	@SuppressWarnings("resource")
	Scanner sc=new Scanner(System.in);
	System.out.println("������һ������");
	String a=sc.next();
	System.out.println("��������һ������");
	String b=sc.next();
	String s=add(a,b);
	System.out.println("��ӵĴ���Ϊ��"+s);
	}
}