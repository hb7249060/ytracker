package org.apache.ydata.service.hub;

import org.apache.ydata.model.hub.HubRechargeRecord;
import org.apache.ydata.service.BaseService;

public interface HubRechargeRecordService extends BaseService {

    int insert(HubRechargeRecord hubInfo);

}
