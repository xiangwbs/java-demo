package com.xwbing.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明: 测试
 * 创建日期: 2017年3月29日 上午9:19:20
 * 作者: xwb
 */

public class ATest {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        String dd = map.get("dd");
        if(map.containsKey("qq")){
            System.out.println("ww");
        }else {
            System.out.println("www");
        }

    }
}
