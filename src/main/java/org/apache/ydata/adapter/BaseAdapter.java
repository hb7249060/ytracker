package org.apache.ydata.adapter;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ydata.model.hub.HubInfo;

public interface BaseAdapter {

    //查询数据
    JSONObject stat(HubInfo hubInfo, String statDate);


}
