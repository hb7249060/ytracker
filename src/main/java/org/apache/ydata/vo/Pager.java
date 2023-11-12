package org.apache.ydata.vo;

import java.util.ArrayList;
import java.util.List;

public class Pager<T> {
    private int sEcho; //访问次数
    private long iTotalRecords;  //总记录数
    private long iTotalDisplayRecords;  //要展示的总记录数
    private int iDisplayStart;  //开始索引
    private int iDisplayLength;  //pageSize

    private String statDesc;    //统计信息

    @SuppressWarnings("rawtypes")
    private List aaData = new ArrayList();

    public Pager() {
        super();
    }

    public Pager<T> wrapPager(Page<T> page) throws Exception{

        this.setiTotalRecords(page.getCount());
        this.setiTotalDisplayRecords(page.getCount());
        this.setAaData(page.getList());
        this.setStatDesc(page.getStatDesc());
        return this;
    }

    public String getStatDesc() {
        return statDesc;
    }

    public void setStatDesc(String statDesc) {
        this.statDesc = statDesc;
    }

    public int getsEcho() {
        return sEcho;
    }

    public void setsEcho(int sEcho) {
        this.sEcho = sEcho;
    }

    public long getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public List getAaData() {
        return aaData;
    }

    public void setAaData(List aaData) {
        this.aaData = aaData;
    }
}