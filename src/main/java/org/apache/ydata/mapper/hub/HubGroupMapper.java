package org.apache.ydata.mapper.hub;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.model.hub.HubGroup;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HubGroupMapper extends BaseMapper<HubGroup> {
}
