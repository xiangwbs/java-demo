package com.xwbing.demo;
import java.util.Random;
import java.util.Scanner;
public class GussNumber {
	/**
	 * 
	 * 功能描述： 数字猜大小
	 * 作    者：xwb
	 * 创建时间：2016年12月9日  下午2:56:45
	 */
	public static void main(String[] args) {
		Random ran=new Random();
		Scanner sc=new Scanner(System.in);
	    int a = ran.nextInt(100) + 1;
	    System.out.println(a);
		int b;
		int times=0;
		do{
			System.out.println("请输入一个数");
			b=sc.nextInt();
			if(b==0){
				break;
			}
			if(b>a){
				System.out.println("大了");
			}else if(b<a){
				System.out.println("小了");
			}
			times++;
		}while(b!=a);
		if(b==a){
			System.out.println("猜对了"+b+"  次数"+ times);
		}else{
			System.out.println("over");
		}
		
	}

}
