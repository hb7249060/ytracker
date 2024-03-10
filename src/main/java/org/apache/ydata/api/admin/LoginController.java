package org.apache.ydata.api.admin;

import com.alibaba.fastjson2.JSONObject;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.ydata.controller.Constants;
import org.apache.ydata.model.common.Admin;
import org.apache.ydata.model.common.AdminLog;
import org.apache.ydata.service.common.AdminLogService;
import org.apache.ydata.service.common.AdminService;
import org.apache.ydata.utils.CommonUtil;
import org.apache.ydata.utils.GoogleAuthenticationTool;
import org.apache.ydata.utils.IdUtil;
import org.apache.ydata.utils.RSAUtils;
import org.apache.ydata.vo.admin.AdminLoginVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
public class LoginController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private AdminService adminService;

    @Resource
    private AdminLogService adminLogService;

    @Resource
    private IdUtil idUtil;

    @Value("${config.prod}")
    private Boolean prod;
    /**
     * 获取系数和指数
     * @return
     * @throws Exception
     */
    @RequestMapping("/keyPair")
    @ResponseBody
    public Map<String,String> keyPair() throws Exception{
        Map<String,String> publicKeyMap = RSAUtils.getPublicKeyMap();
        return publicKeyMap;
    }

    @PostMapping("/login")
    public Object login(AdminLoginVo vo, RedirectAttributes redirectAttributes) {
        try {
            vo.setUsername(URLDecoder.decode(RSAUtils.decryptStringByJs(vo.getUsername()), "UTF-8"));
            vo.setPassword(URLDecoder.decode(RSAUtils.decryptStringByJs(vo.getPassword()), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("admin login username:{}, password:{}, googlecode:{}", vo.getUsername(), "******", vo.getGoogleCode());
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(vo.getUsername(), vo.getPassword());
        try {
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            writeLoginLog("登录失败，用户名错误", "输入的用户名：" + vo.getUsername());
            request.setAttribute("errMsg", "用户名错误");
            redirectAttributes.addAttribute("errMsg", "用户名或密码错误");
            return "redirect:" + Constants.ERROR;
        } catch (IncorrectCredentialsException e) {
            writeLoginLog("登录失败，密码错误", "输入的用户名：" + vo.getUsername() + "，输入的密码：" + vo.getPassword());
            request.setAttribute("errMsg", "密码错误");
            redirectAttributes.addAttribute("errMsg", "用户名或密码错误");
            return "redirect:" + Constants.ERROR;
        }

        Admin admin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        if (admin == null) {
            writeLoginLog("登录失败，用户名错误", "输入的用户名：" + vo.getUsername());
            request.setAttribute("errMsg", "用户名或密码错误");
//            redirectAttributes.addAttribute("errMsg", "用户名错误");
//            return "redirect:" + Constants.ERROR;
            return "errmsg";
        }

        if (admin.isLocked()) {
            writeLoginLog("登录失败，账户已被禁用", "用户名：" + vo.getUsername());
            request.setAttribute("errMsg", "账户已被禁用");
            redirectAttributes.addAttribute("errMsg", "账户已被禁用");
            return "redirect:" + Constants.ERROR;
        }

        //没有32位随机码的情况
        if (ObjectUtils.isEmpty(admin.getTwoFactorCode())) {
            //前往code绑定页面
            redirectAttributes.addAttribute("aspm", admin.getId());
            //todo 处理设计该页面绑定谷歌认证码（QR二维码）
            return "redirect:/admin/bindingGoogleTwoFactorValidate";
        }

//        if(prod) {
//            //校验google code
//            String rightGoogleCode = GoogleAuthenticationTool.getTOTPCode(admin.getTwoFactorCode());
//            if (ObjectUtils.isEmpty(vo.getGoogleCode())) {
//                request.setAttribute("errMsg", "请输入谷歌验证码");
//                redirectAttributes.addAttribute("errMsg", "请输入谷歌验证码");
//                return "redirect:" + Constants.ERROR;
//            }
//            if (!vo.getGoogleCode().equals(rightGoogleCode)) {
//                writeLoginLog("登录失败，谷歌验证码不正确或已超时", "用户名：" + vo.getUsername());
//                request.setAttribute("errMsg", "谷歌验证码不正确或已超时");
//                redirectAttributes.addAttribute("errMsg", "谷歌验证码不正确或已超时");
//                return "redirect:" + Constants.ERROR;
//            }
//        }
        //写log
        if(admin.getCreated() != null) {
            writeLoginLog("超管登录成功", "用户名：" + vo.getUsername());
        }
        log.info("超管登录成功, 用户名：{}", vo.getUsername());
        request.getSession().setAttribute("AdminUser", admin);

        return "redirect:" + Constants.ADMIN_INDEX;
    }

    @GetMapping("/logout")
    public Object login() {
        SecurityUtils.getSubject().logout();
        request.getSession().invalidate();
        return "redirect:" + Constants.ADMIN_LOGIN;
    }

    private void writeLoginLog(String content, String memo) {
        AdminLog adminLog = new AdminLog();
        adminLog.setId(idUtil.nextId());
        adminLog.setEvent("AdminLogin");
        adminLog.setEventDesc("管理员登录");
        adminLog.setIp(CommonUtil.getClientIp());
        adminLog.setContent(content);
        adminLog.setMemo(memo);
        adminLog.setCreated(System.currentTimeMillis());
        adminLogService.getMapper().insert(adminLog);
    }

    /**
     * 前往谷歌两步验证绑定页面
     *
     * @param adminId
     * @return
     */
    @GetMapping("/bindingGoogleTwoFactorValidate")
    public String toBindingGoogleTwoFactorValidate(@RequestParam("aspm") long adminId) {
        String randomSecretKey = GoogleAuthenticationTool.generateSecretKey();
        Admin admin = (Admin) adminService.getMapper().selectByPrimaryKey(adminId);
        //此步设置的参数就是App扫码后展示出来的参数
        String qrCodeString = GoogleAuthenticationTool.spawnScanQRString(admin.getUsername(), randomSecretKey, "聚合数据");
        String qrCodeImageBase64 = null;
        try {
            qrCodeImageBase64 = GoogleAuthenticationTool.createQRCode(qrCodeString, null, 512, 512);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        request.setAttribute("aspm", adminId);
        request.setAttribute("randomSecretKey", randomSecretKey);
        request.setAttribute("qrCodeImageBase64", qrCodeImageBase64);

        return "bindingGoogleTwoFactorValidate";
    }

    /**
     * 执行谷歌两步验证绑定
     *
     * @return
     */
    @PostMapping("/bindingGoogleTwoFactorValidate")
    @ResponseBody
    public String bindingGoogleTwoFactorValidate(@RequestParam("aspm") Long aspm, @RequestParam("randomSecretKey") String secretKey, @RequestParam("inputGoogleCode") String googleCode) {
        JSONObject respJsonObj = new JSONObject();
        Admin admin = (Admin) adminService.getMapper().selectByPrimaryKey(aspm);
        if (admin.getTwoFactorCode() != null && !admin.getTwoFactorCode().equals("")) {
            respJsonObj.put("state", "fail");
            respJsonObj.put("msg", "该用户已经绑定了，不可重复绑定，若不慎删除令牌，请联系管理员重置");
            return respJsonObj.toString();
        }
        String rightCode = GoogleAuthenticationTool.getTOTPCode(secretKey);
        if (!rightCode.equals(googleCode)) {
            respJsonObj.put("state", "fail");
            respJsonObj.put("msg", "验证码失效或错误，请重试");
            return respJsonObj.toString();
        }
        admin.setTwoFactorCode(secretKey);
        int res = adminService.getMapper().updateByPrimaryKeySelective(admin);

        if (res > 0) {
            respJsonObj.put("state", "success");
        } else {
            respJsonObj.put("state", "fail");
            respJsonObj.put("msg", "数据库操作失败");
        }
        return respJsonObj.toString();
    }
}
