package org.apache.ydata.service.common;

import org.apache.ydata.model.common.Admin;
import org.apache.ydata.service.BaseService;

public interface AdminService extends BaseService {

    Admin findByUsername(String username);

}
