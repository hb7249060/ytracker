package org.apache.ydata.mapper;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface BaseMapper<T> extends Mapper<T>, tk.mybatis.mapper.common.BaseMapper<T>, ConditionMapper<T>, InsertListMapper {

    T selectOneBySid(String sid);

}
