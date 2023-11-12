package org.apache.ydata.service.hub;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ydata.model.hub.HubData;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.BaseService;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;

import java.text.ParseException;
import java.util.List;

public interface HubDataService extends BaseService {

    HubData insertOrUpdate(HubInfo hubInfo, JSONObject jsonDataObject);

    List<HubData> selectByDateAndHubIds(String statDate, List<Long> hubIds);
}
