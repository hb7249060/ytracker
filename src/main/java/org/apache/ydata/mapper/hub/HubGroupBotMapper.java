package org.apache.ydata.mapper.hub;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.model.hub.HubGroupBot;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HubGroupBotMapper extends BaseMapper<HubGroupBot> {

    @Update("DELETE FROM hub_group_bot WHERE group_id=#{groupId}")
    void deleteAllByGroupId(@Param("groupId") Long groupId);

}
