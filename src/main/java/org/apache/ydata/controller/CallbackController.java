package org.apache.ydata.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
@Controller(value = "CallbackOpenApi")
@RequestMapping(value = "/callback")
public class CallbackController {

    @Autowired
    private HttpServletRequest request;

    @ResponseBody
    @RequestMapping(value = "/aliDmfNotify.htm")
    public Object aliDmfNotify() {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            log.info("aliDmfNotify paramName={}, paramValue={}", name, request.getParameter(name));
        }
        return ResultGenerator.success();
    }

}
