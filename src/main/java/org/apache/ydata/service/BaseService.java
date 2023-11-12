package org.apache.ydata.service;

import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;

public interface BaseService {

    BaseMapper getMapper();

    PageResult getListByPageRequest(PageRequest pageRequest);

}
