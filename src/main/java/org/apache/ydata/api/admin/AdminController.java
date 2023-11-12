package org.apache.ydata.api.admin;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.ydata.model.common.Admin;
import org.apache.ydata.service.common.AdminService;
import org.apache.ydata.utils.*;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.apache.ydata.vo.PageVo;
import org.apache.ydata.vo.admin.AdminInfoVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping(value = "/admin/system/admin")
public class AdminController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private AdminService adminService;

    @Resource
    private IdUtil idUtil;

    @Value("${config.prod}")
    private Boolean prod;

    @PostMapping(value = "/create")
    public Object create(Admin vo) throws UnsupportedEncodingException {
        log.info("创建超管信息：{}", JSONObject.toJSONString(vo));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");
        if(ObjectUtils.isEmpty(vo.getUsername()) || ObjectUtils.isEmpty(vo.getPassword())) {
            log.info("账户密码不可为空！");
            return Tools.createResponse(ResponseCode.ERROR, "账户密码不可为空！", null);
        }
        //校验重名
        if(adminService.findByUsername(vo.getUsername().trim()) != null) {
            log.info("创建失败，存在重名账户！");
            return Tools.createResponse(ResponseCode.ERROR, "创建失败，存在重名账户！", null);
        }
        if(prod) {
            //校验当前登录用户的google验证器
            String rightGoogleCode = GoogleAuthenticationTool.getTOTPCode(optAdmin.getTwoFactorCode());
            if (ObjectUtils.isEmpty(vo.getGoogleCode())) {
                log.info("请输入谷歌验证码！");
                return Tools.createResponse(ResponseCode.ERROR, "请输入谷歌验证码", null);
            }
            if (!vo.getGoogleCode().equals(rightGoogleCode)) {
                log.info("创建失败，谷歌验证码不正确或已超时，超管用户名：{}", optAdmin.getUsername());
                return Tools.createResponse(ResponseCode.ERROR, "谷歌验证码不正确或已超时", null);
            }
        }

        Admin admin = new Admin();
        admin.setId(idUtil.nextId());
        admin.setUsername(vo.getUsername());
        //密码加密存储
        String salt = Tools.getSalt(8);
        admin.setSalt(salt);
        admin.setPassword(new Md5Hash(vo.getPassword(), salt, 1024).toHex());
        admin.setMemo(vo.getMemo());
        admin.setCreated(System.currentTimeMillis());
        admin.setState(vo.getState());
        adminService.getMapper().insert(admin);
        return Tools.createResponse(ResponseCode.SUCCESS, "创建成功", null);
    }

    @RequestMapping(value = "/getList")
    public Object getList(@RequestBody PageVo pageVo) {
        PageResult pageResult = adminService.getListByPageRequest(PageRequest.get(pageVo));
        return PageUtils.getPager(pageResult);
    }

    @RequestMapping(value = "/getListByPage/{pageNum}/{pageSize}")
    public Object getListByPage(int pageNum, int pageSize) {
        PageResult pageResult = adminService.getListByPageRequest(new PageRequest(pageNum, pageSize));
        return pageResult;
    }

    @GetMapping(value = "/getOne/{primaryKey}")
    public Object getOne(@PathVariable Object primaryKey) {
        Admin admin = (Admin) adminService.getMapper().selectByPrimaryKey(primaryKey);
        return Tools.createResponse(ResponseCode.SUCCESS,null, admin);
    }

    @PostMapping(value = "/update")
    public Object update(AdminInfoVo vo) {
        log.info("更新超管信息：{}", JSONObject.toJSONString(vo));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");

        if(prod) {
            //校验当前登录用户的google验证器
            String rightGoogleCode = GoogleAuthenticationTool.getTOTPCode(optAdmin.getTwoFactorCode());
            if (ObjectUtils.isEmpty(vo.getGoogleCode())) {
                log.info("请输入谷歌验证码！");
                return Tools.createResponse(ResponseCode.ERROR, "请输入谷歌验证码", null);
            }
            if (!vo.getGoogleCode().equals(rightGoogleCode)) {
                log.info("更新失败，谷歌验证码不正确或已超时，超管用户名：{}", optAdmin.getUsername());
                return Tools.createResponse(ResponseCode.ERROR, "谷歌验证码不正确或已超时", null);
            }
        }

        if(vo.getId() == null) {
            return Tools.createResponse(ResponseCode.ERROR, null, null);
        }
        Admin admin = (Admin) adminService.getMapper().selectByPrimaryKey(vo.getId());
        if(admin == null) {
            return Tools.createResponse(ResponseCode.ERROR, "数据不存在", null);
        }
        if(admin.getId().longValue() != optAdmin.getId().longValue()) {
            log.info("不可修改其它超管信息！");
            return Tools.createResponse(ResponseCode.ERROR, "不可修改其它超管信息", null);
        }
        admin.setUsername(vo.getUsername());
        if(!ObjectUtils.isEmpty(vo.getPassword())) {
            //修改密码
            String salt = Tools.getSalt(8);
            admin.setSalt(salt);
            admin.setPassword(new Md5Hash(vo.getPassword(), salt, 1024).toHex());
        } else {
            admin.setPassword(null); //防止update后变空
        }
        if(!ObjectUtils.isEmpty(vo.getSecPwd())) {
            //修改二级密码
            String salt = Tools.getSalt(8);
            admin.setSecSalt(salt);
            admin.setSecPwd(new Md5Hash(vo.getSecPwd(), salt, 1024).toHex());
        } else {
            admin.setSecPwd(null); //防止update后变空
        }
        admin.setMemo(vo.getMemo());
        admin.setState(vo.getState());
        admin.setUpdated(System.currentTimeMillis());
        int rs = adminService.getMapper().updateByPrimaryKeySelective(admin);
        if(rs > 0) {
            if(!ObjectUtils.isEmpty(admin.getSecPwd())) {
                optAdmin.setSecPwd(admin.getSecPwd());
                optAdmin.setSecSalt(admin.getSecSalt());
            }
        }
        return Tools.createResponse(ResponseCode.SUCCESS, null, admin);
    }

    @DeleteMapping(value = "/deleteOne/{primaryKey}")
    public Object deleteOne(@PathVariable Object primaryKey, String secPwd) {
        log.info("删除管理员，安全密码：{}", secPwd);
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");

        if(prod) {
            //校验安全密码
            if (ObjectUtils.isEmpty(secPwd)) {
                return Tools.createResponse(ResponseCode.ERROR, "请输入安全密码", null);
            }
            if (ObjectUtils.isEmpty(optAdmin.getSecPwd())) {
                return Tools.createResponse(ResponseCode.ERROR, "请先设置安全密码", null);
            }
            if (!optAdmin.getSecPwd().equals(new Md5Hash(secPwd, optAdmin.getSecSalt(), 1024).toHex())) {
                log.info("删除管理员失败，安全密码不正确，超管用户名：{}", optAdmin.getUsername());
                return Tools.createResponse(ResponseCode.ERROR, "安全密码错误！", null);
            }
        }

        Admin admin = (Admin) adminService.getMapper().selectByPrimaryKey(primaryKey);
        if(admin.getCreated() == null) {
            return Tools.createResponse(ResponseCode.ERROR,"超管无法删除", null);
        }
        if(admin.getId().longValue() != optAdmin.getId().longValue()) {
            return Tools.createResponse(ResponseCode.ERROR, "不可删除其它超管", null);
        }

        adminService.getMapper().deleteByPrimaryKey(primaryKey);
        request.getSession().invalidate();
        return Tools.createResponse(ResponseCode.SUCCESS,"删除成功", null);
    }
}
