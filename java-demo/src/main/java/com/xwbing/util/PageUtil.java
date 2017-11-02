package com.xwbing.util;

import java.util.List;

/**
 * 创建时间: 2016/10/24 9:41
 * 作者: xiangwb
 * 说明: 分页util
 */
public class PageUtil {
    private Integer pageNo = 1;
    private Integer pageSize = 20;
    private List<?> data;
    private boolean success = true;
    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 总数
     */
    private Integer count;
    /**
     * 错误提示
     */
    private String message;


    public PageUtil() {
    }

    public PageUtil(int pageSize, int pageNo) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageUtil(int pageSize) {
        this.pageNo = 1;
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean isSuccess) {
        this.success = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
