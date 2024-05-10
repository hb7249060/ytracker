package org.apache.ydata.mapper.hub;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.model.hub.HubPayAddrRecord;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HubPayAddrRecordMapper extends BaseMapper<HubPayAddrRecord> {

}
