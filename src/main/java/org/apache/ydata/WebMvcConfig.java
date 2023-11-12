package org.apache.ydata;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        List<String> adminExcludeUrls = new ArrayList<>();
//        adminExcludeUrls.add("/admin/login.html");
//        adminExcludeUrls.add("/admin/login");
//        adminExcludeUrls.add("/admin/bindingGoogleTwoFactorValidate");
//        registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**").excludePathPatterns(adminExcludeUrls);

//        List<String> mchAdminExcludeUrls = new ArrayList<>();
//        mchAdminExcludeUrls.add("/mchadmin/login.html");
//        mchAdminExcludeUrls.add("/mchadmin/login");
//        mchAdminExcludeUrls.add("/mchadmin/bindingGoogleTwoFactorValidate");
//        registry.addInterceptor(merchantInterceptor).addPathPatterns("/mchadmin/**").excludePathPatterns(mchAdminExcludeUrls);
    }
}
