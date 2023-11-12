package org.apache.ydata.api.admin;

import org.apache.ydata.service.hub.HubDataService;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.apache.ydata.vo.PageVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/admin/business/stat")
public class StatController {

    @Resource
    private HubDataService hubDataService;

    @RequestMapping(value = "/getList")
    public Object getList(@RequestBody PageVo pageVo) {
        PageResult pageResult = hubDataService.getListByPageRequest(PageRequest.get(pageVo));
        return PageUtils.getPager(pageResult);
    }
}
