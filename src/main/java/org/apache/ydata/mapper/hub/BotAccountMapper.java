package org.apache.ydata.mapper.hub;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.model.hub.BotAccount;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BotAccountMapper extends BaseMapper<BotAccount> {
}
