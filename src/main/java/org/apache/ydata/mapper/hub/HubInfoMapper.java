package org.apache.ydata.mapper.hub;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.model.hub.HubInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HubInfoMapper extends BaseMapper<HubInfo> {

    @Select({
            "<script>",
            "SELECT * FROM hub_info",
            "WHERE id IN",
            "<foreach collection='userIdList' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "ORDER BY id DESC",
            "</script>"
    })
    List<HubInfo> selectByIds(@Param("userIdList") List<Long> userIdList);

    @Select({
            "<script>",
            "SELECT SUM(total_points) FROM hub_info",
            "WHERE id IN",
            "<foreach collection='userIdList' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "ORDER BY id DESC",
            "</script>"
    })
    Double sumTotalPointsByIds(@Param("userIdList") List<Long> userIdList);

    @Update("UPDATE user_info SET state=#{state} WHERE parent_ids LIKE '%${id}%' AND state<>-1")
    void updateChildState(@Param("id") Long id, @Param("state") Integer state);
}
