package org.apache.ydata.service.hub;

import org.apache.ydata.model.hub.HubPayAddrRecord;
import org.apache.ydata.service.BaseService;

public interface HubPayAddrRecordService extends BaseService {

    int insert(HubPayAddrRecord record);

}
