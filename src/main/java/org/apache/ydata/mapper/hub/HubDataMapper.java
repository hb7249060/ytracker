package org.apache.ydata.mapper.hub;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.model.hub.HubData;
import org.apache.ydata.model.hub.HubInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HubDataMapper extends BaseMapper<HubData> {

}
