package org.apache.ydata.service.hub;

import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.BaseService;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;

import java.text.ParseException;
import java.util.List;

public interface HubInfoService extends BaseService {

    HubInfo selectByName(String name);

    List<HubInfo> selectByIds(List<Long> hubIdList);

    PageResult stat(PageRequest pageRequest) throws ParseException;

    int insert(HubInfo userinfo);

    HubInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(HubInfo userinfo);

    void updateByPrimaryKeySelective(HubInfo userInfo);

    List<HubInfo> selectAll();

}
