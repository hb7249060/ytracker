package org.apache.ydata.mapper.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.model.common.AdminLog;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminLogMapper extends BaseMapper<AdminLog> {

}
