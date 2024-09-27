package org.apache.ydata.vo;

public class PageVo {
    private Integer sEcho;  //第几次请求的标志，虽然没什么用，但是最好有
    private int iColumns;
    private int iDisplayStart;
    private int iDisplayLength;
    private String datemin;
    private String datemax;
    private String searchKey1;
    private String searchKey2;
    private String searchKey3;
    private String searchKey4;
    private String searchKey5;
    private String orderNo;
    private String orderState;
    private String hubId;
    private String userIp;
    private String userName;
    private String eventType;
    private String checkCode;
    private String channelCode;
    private Long channelId;
    private Long mchId;
    private String mchName;
    private String statDate;
    private Integer state;
    private String alias;
    private int seeMins = -1;
    private Double minpoints;
    private Double maxpoints;
    private Long parentId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Double getMinpoints() {
        return minpoints;
    }

    public void setMinpoints(Double minpoints) {
        this.minpoints = minpoints;
    }

    public Double getMaxpoints() {
        return maxpoints;
    }

    public void setMaxpoints(Double maxpoints) {
        this.maxpoints = maxpoints;
    }

    public int getSeeMins() {
        return seeMins;
    }

    public void setSeeMins(int seeMins) {
        this.seeMins = seeMins;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDatemin() {
        return datemin;
    }

    public void setDatemin(String datemin) {
        this.datemin = datemin;
    }

    public String getDatemax() {
        return datemax;
    }

    public void setDatemax(String datemax) {
        this.datemax = datemax;
    }

    public String getSearchKey1() {
        return searchKey1;
    }

    public void setSearchKey1(String searchKey1) {
        this.searchKey1 = searchKey1;
    }

    public String getSearchKey2() {
        return searchKey2;
    }

    public void setSearchKey2(String searchKey2) {
        this.searchKey2 = searchKey2;
    }

    public String getSearchKey3() {
        return searchKey3;
    }

    public void setSearchKey3(String searchKey3) {
        this.searchKey3 = searchKey3;
    }

    public String getSearchKey4() {
        return searchKey4;
    }

    public void setSearchKey4(String searchKey4) {
        this.searchKey4 = searchKey4;
    }

    public String getSearchKey5() {
        return searchKey5;
    }

    public void setSearchKey5(String searchKey5) {
        this.searchKey5 = searchKey5;
    }

    public int getCurrentPageNum() {
        return iDisplayStart/iDisplayLength+1;
    }
    public void setCurrentPageNum(int currentPageNum) {
    }
    public Integer getsEcho() {
        return sEcho;
    }
    public void setsEcho(Integer sEcho) {
        this.sEcho = sEcho;
    }
    public int getiColumns() {
        return iColumns;
    }
    public void setiColumns(int iColumns) {
        this.iColumns = iColumns;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}