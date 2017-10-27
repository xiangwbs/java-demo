package com.xwbing.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称: java-demo
 * 创建时间: 2017/10/27 13:34
 * 作者: xiangwb
 * 说明: 集合数据分页
 */
public class ListPageUtil {
    public static Map<String, Object> page(List<Object> list, int currentPage, int pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        int size = list.size();
        pageMap.put("count", size);
        int start = (currentPage - 1) * pageSize;
        int end = pageSize * currentPage;
        if (start >= end || start >= size) {
            pageMap.put("data", Collections.emptyList());
        } else {
            if (start < size && end > size) {
                end = size;
            }
            pageMap.put("data", list.subList(start, end));
        }
        int totalPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
        pageMap.put("totalPage", totalPage);
        return pageMap;
    }
}
