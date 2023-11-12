package org.apache.ydata.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页类
 * @version 1.0
 * @create 2017/6/29 18:32
 **/
public class Page<T> implements Serializable {

    /**
     * 当前页
     */
    private int pageNo = 1;
    /**
     * 页面条数
     */
    private int pageSize = 10;
    /**
     * 总数
     */
    private long count;
    /**
     * 第一页
     */
    private int first;
    /**
     * 最后一页
     */
    private int last;
    /**
     * 前一页
     */
    @SuppressWarnings("unused")
    private int prev;
    /**
     * 后一页
     */
    @SuppressWarnings("unused")
    private int next;

    /**
     * 是否第一页
     */
    private boolean firstPage;
    /**
     * 是否最后一条
     */
    private boolean lastPage;

    private String statDesc;    //统计信息
    private List<T> list = new ArrayList<T>();

    public Page() {
        this.pageSize = -1;
    }

    public String getStatDesc() {
        return statDesc;
    }

    public void setStatDesc(String statDesc) {
        this.statDesc = statDesc;
    }

    public Page(int pageNo, int pageSize) {
        this(pageNo, pageSize, 0);
    }

    public Page(int pageNo, int pageSize, long count) {
        this(pageNo, pageSize, count, new ArrayList<T>());
    }

    public Page(int pageNo, int pageSize, long count, List<T> list) {
        this.setCount(count);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
        this.list = list;
    }

    public void initialize() {

        this.first = 1;

        this.last = (int) (count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);

        if (this.count % this.pageSize != 0 || this.last == 0) {
            this.last++;
        }

        if (this.last < this.first) {
            this.last = this.first;
        }

        if (this.pageNo <= 1) {
            this.pageNo = this.first;
            this.firstPage = true;
        }

        if (this.pageNo >= this.last) {
            this.pageNo = this.last;
            this.lastPage = true;
        }

        if (this.pageNo < this.last - 1) {
            this.next = this.pageNo + 1;
        } else {
            this.next = this.last;
        }

        if (this.pageNo > 1) {
            this.prev = this.pageNo - 1;
        } else {
            this.prev = this.first;
        }

        // 2
        if (this.pageNo < this.first) {
            this.pageNo = this.first;
        }

        if (this.pageNo > this.last) {
            this.pageNo = this.last;
        }

    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
        if (pageSize >= count) {
            pageNo = 1;
        }
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? 10 : pageSize;
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

    public int getTotalPage() {
        return getLast();
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public int getPrev() {
        if (isFirstPage()) {
            return pageNo;
        } else {
            return pageNo - 1;
        }
    }

    public int getNext() {
        if (isLastPage()) {
            return pageNo;
        } else {
            return pageNo + 1;
        }
    }

    public List<T> getList() {
        return list;
    }

    public Page<T> setList(List<T> list) {
        this.list = list;
        initialize();
        return this;
    }

}