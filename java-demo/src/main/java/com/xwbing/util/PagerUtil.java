package com.xwbing.util;

import java.util.List;

/**
 * drore专用
 */
public class PagerUtil {
    private Integer count;
    private Integer totalPage;
    private String message;
    private Integer currentPage = 1;
    private Integer pageSize = 20;
    private List<?> data;
    private boolean success = true;

    public PagerUtil() {
    }

    public PagerUtil(int pageSize, int currentPage) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public PagerUtil(int pageSize) {
        this.currentPage = 1;
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
