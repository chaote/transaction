package com.htsc.transaction.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * @ author: chaote
 * @ date: 2025/8/21  16:35
 * @ <p>分页响应结果<p>
 */
public class PageResponse<T> implements Serializable {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private List<T> content;

    public PageResponse(int currentPage, int pageSize, int totalPages, long totalElements, List<T> content) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.content = content;
    }

    public static <T>PageResponse<T> convertPage(List<T> content, int currentPage, int pageSize, long totalElements){
        int totalPages = (int)Math.ceil((double) totalElements/pageSize);
        return new PageResponse<>(currentPage,pageSize,totalPages,totalElements,content);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PageResponse{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", content=" + content +
                '}';
    }
}
