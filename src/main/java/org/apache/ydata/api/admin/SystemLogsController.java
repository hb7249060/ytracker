package org.apache.ydata.api.admin;

import org.apache.ydata.service.common.AdminLogService;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.apache.ydata.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/system/logs")
public class SystemLogsController {

    @Autowired
    private AdminLogService adminLogService;

    @RequestMapping(value = "/getList")
    public Object getList(@RequestBody PageVo pageVo) {
        PageResult pageResult = adminLogService.getListByPageRequest(PageRequest.get(pageVo));
        return PageUtils.getPager(pageResult);
    }

    @RequestMapping(value = "/getListByPage/{pageNum}/{pageSize}")
    public Object getListByPage(int pageNum, int pageSize) {
        PageResult pageResult = adminLogService.getListByPageRequest(new PageRequest(pageNum, pageSize));
        return pageResult;
    }

}
