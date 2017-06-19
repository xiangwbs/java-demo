package com.xwbing.util;

import java.util.List;

public class PageUtil {
    private Integer count;
    private Integer totalPage;
    private String message;
    private Integer currentPage = 1;
    private Integer pageSize = 20;
    private List<?> root;
    private boolean success = true;

    public PageUtil() {
    }

    public PageUtil(int pageSize, int currentPage) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public PageUtil(int pageSize) {
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

    public List<?> getRoot() {
        return root;
    }

    public void setRoot(List<?> root) {
        this.root = root;
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
