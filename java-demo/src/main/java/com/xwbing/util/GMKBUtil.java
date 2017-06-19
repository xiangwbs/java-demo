package com.xwbing.util;

/**
 * 说明: 容量单位换算<br/>
 * 创建日期: 2016年12月6日 下午3:20:58 <br/>
 * 作者: xwb
 */

public class GMKBUtil {
	/**
	 * 
	 * 功能描述： 容量单位换算成B KB MB GB <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2016年12月6日 下午4:08:57 <br/>
	 * 
	 * @param size
	 * @return
	 */
	public static String convertCapacity(double size) {
		double kb = 1024;
		double mb = kb * 1024;
		double gb = mb * 1024;

		if (size >= gb) {
			size = size / gb;
			return String.format("%.2f GB", size);//四舍五入保留2位小数
		} else if (size >= mb) {
			size = size / mb;
			return String.format("%.2f MB", size);
		} else if (size >= kb) {
			size = size / kb;
			return String.format("%.2f KB", size);

		} else
			return String.format("%f B", size);
	}

	public static void main(String[] args) {
		double d = 10241024.0;
		System.out.println(convertCapacity(d));
	}
}
