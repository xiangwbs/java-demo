package com.xwbing.demo;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 遍历map
 * 三种方式：
 * 遍历所有的key
 * 遍历所有的key-value对
 * 遍历所有的value（相对不常用）
 * 
 * @author soft01
 *
 */
public class ForEachMap {
	public static void main(String[] args) {
		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("语文", 99);
		map.put("数学", 98);
		map.put("英语", 97);
		map.put("物理", 96);
		map.put("化学", 99);
		System.out.println(map);
		
		
		
		/*
		 * 遍历所有的key
		 * set<k>   keySet()
		 * 该方法会将当前map中所有的key存入一个set集合后返回
		 * 那么遍历该集合就等于遍历类所有的key
		 */
		Set<String> keySet=map.keySet();
		for(String key:keySet){
			System.out.println(key);
		}
		
		/*
		 * 遍历每一组键值对（推荐）
		 * map中每一组键值对都是由map的内部类：
		 * java.util.map.entry的一个实例类表示的
		 * entry有两个方法：getkey  getvalue 可以分别获取这一组键值对中的key和value
		 * 
		 * Set<Entry> entrySet
		 * 该方法会将map中的每一组键对（entry实例）
		 * 存入一个set集合后返回
		 */
		Set<Entry<String,Integer>>   entrySet=map.entrySet();
		for(Entry<String,Integer> e:entrySet){
			String key=e.getKey();
			Integer value=e.getValue();
			System.out.println(key+":"+value);
		}
		
		/*
		 * 遍历所有的value
		 * Collection values()
		 * 该方法会将当前map中所有的value存入一个集合后返回
		 */
		Collection<Integer> values=map.values();
		for(Integer value:values){
			System.out.println(value);
		}
		
		
		/**
		 * jdk1.8
		 */
		map.forEach((k,v)->{
			System.out.println("key : " + k + "; value : " + v);
		});
		
	}
}
