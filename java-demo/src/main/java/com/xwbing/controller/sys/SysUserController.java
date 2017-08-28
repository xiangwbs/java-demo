package com.xwbing.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.Exception.BusinessException;
import com.xwbing.Exception.InvalidPassWordException;
import com.xwbing.entity.*;
import com.xwbing.service.sys.*;
import com.xwbing.shiro.UsernamePasswordCaptchaToken;
import com.xwbing.util.CommonEnum.LoginInOut;
import com.xwbing.util.CommonEnum.MenuOrButton;
import com.xwbing.util.CommonEnum.YesOrNo;
import com.xwbing.util.IpUtil;
import com.xwbing.util.JSONObjResult;
import com.xwbing.util.LoginSysUserUtil;
import com.xwbing.util.RestMessage;
import com.xwbing.util.captcah.CaptchaException;
import com.xwbing.validate.FormMap;
import com.xwbing.validate.ValidateResult;
import com.xwbing.validate.ValidateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 说明: 用户控制器 <br/>
 * 创建日期: 2017年1月18日 下午5:57:18 <br/>
 * 作者: xwb
 */
@Controller
@RequestMapping("/user/")
public class SysUserController {
    private Logger log = LoggerFactory.getLogger(SysUserController.class);
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    SysUserLoginInOutService sysUserLoginInOutService;
    @Autowired
    SysAuthorityService sysAuthorityService;

    @RequestMapping("loginCheck")
    @ResponseBody
    public JSONObject loginCheck(SysUser sysUser, boolean rememberMe,
            @RequestParam String captcha, HttpServletRequest request) {
        String logMsg = "loginCheck";
        log.info(logMsg + "start");
        try {
            // 先判断是不是为空
            if (StringUtils.isEmpty(sysUser.getUserName())
                    || StringUtils.isEmpty(sysUser.getPassword())) {

                return JSONObjResult.toJSONObj("用户名或密码不能为空!");
            }
            Subject subject = SecurityUtils.getSubject();
            String ip = IpUtil.getIpAddr(request);
            UsernamePasswordCaptchaToken token = new UsernamePasswordCaptchaToken(
                    sysUser.getUserName(), sysUser.getPassword().toCharArray(),
                    rememberMe, ip, captcha);
            token.setRememberMe(rememberMe);
            subject.login(token);
            token.clear();// -============此处测试加上
            if (subject.isAuthenticated()) {
                // 保存登录日志
                sysUser = LoginSysUserUtil.getSysUser();
                // 修改用户的最后登录时间和登录ip
                String userId = sysUser.getId();
                Date now = new Date();
                RestMessage result = sysUserService.updateLoginInfo(userId,
                        ip, now);// 修改最后登录信息
                if (result.isSuccess()) {
                    SysUserLoginInOut inOut = new SysUserLoginInOut();
                    inOut.setInoutType(LoginInOut.IN.getValue());
                    inOut.setIp(ip);
                    inOut.setRecordDate(now);
                    inOut.setUserId(userId);
                    result = sysUserLoginInOutService.save(inOut);// 保存登录日志信息
                    if (result.isSuccess()) {
                        return JSONObjResult.toJSONObj(result);
                    } else {
                        return JSONObjResult.toJSONObj("保存用户登录日志失败");
                    }
                } else {
                    subject.logout();
                    return JSONObjResult.toJSONObj(result.getMsg());
                }
            } else
                return JSONObjResult.toJSONObj("你认证未通过，请重新登录");
        } catch (BusinessException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (CaptchaException e) {
            e.printStackTrace();
            return JSONObjResult.toJSONObj("验证码错误");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return JSONObjResult.toJSONObj("账号或者密码不正确");
        } catch (InvalidPassWordException e) {
            e.printStackTrace();
            return JSONObjResult.toJSONObj("密码已经失效");
        } catch (LockedAccountException e) {
            e.printStackTrace();
            return JSONObjResult.toJSONObj("帐号已被锁");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    /**
     * 当前登录用户修改密码
     * 
     * @param oldPassWord
     * @param newPassWord
     * @return
     */
    @RequestMapping("updatePassWord")
    @ResponseBody
    public JSONObject updatePassWord(@RequestParam String oldPassWord,
            @RequestParam String newPassWord) {
        String logMsg = "当前登录用户修改密码";
        log.info(logMsg);
        try {
            String loginUserId = LoginSysUserUtil.getUserId();
            if (StringUtils.isEmpty(loginUserId))
                return JSONObjResult.toJSONObj("未能获取到登录信息，请重新登录");
            RestMessage result = sysUserService.updatePassWord(newPassWord,
                    oldPassWord, loginUserId);
            return JSONObjResult.toJSONObj(result);
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    /**
     * 保存用户角色
     * 
     * @return
     */
    @RequestMapping("saveRole")
    @ResponseBody
    public JSONObject saveRole(@RequestParam String roleIds,
            @RequestParam String userId) {
        String logMsg = "保存用户角色";
        log.info(logMsg + "ids={}", roleIds);
        try {
            if (StringUtils.isEmpty(roleIds))
                return JSONObjResult.toJSONObj("角色主键不能为空");
            if (StringUtils.isEmpty(userId))
                return JSONObjResult.toJSONObj("用户主键不能为空");

            SysUser old = sysUserService.findById(userId);
            if (old == null) {
                return JSONObjResult.toJSONObj("没有查询到对象");
            }
            if (YesOrNo.YES.getCode().equalsIgnoreCase(old.getIsAdmin())) {
                return JSONObjResult.toJSONObj("不能对管理员进行删除操作");
            }
            String ids[] = roleIds.split(",");
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (String id : ids) {
                SysUserRole re = new SysUserRole();
                re.setRoleId(id);
                re.setUserId(userId);
                list.add(re);
            }
            RestMessage result = sysUserRoleService.saveBatch(list, userId);
            return JSONObjResult.toJSONObj(result);
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    /**
     * 根据用户主键查找所拥有的权限
     * 
     * @param userId
     * @param enable
     * @return
     */
    @RequestMapping("queryAuthority")
    @ResponseBody
    public JSONObject queryAuthority(@RequestParam String userId,
            @RequestParam String enable) {
        String logMsg = "根据用户主键查找所拥有的权限";
        log.info(logMsg);
        if (StringUtils.isEmpty(userId))
            return JSONObjResult.toJSONObj("用户主键不能为空");
        try {
            List<SysAuthority> list = sysUserService.queryAuthority(userId,
                    enable);
            return JSONObjResult.toJSONObj(list, true, "");
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }

    }

    /**
     * 获取当前登录用户信息
     * 
     * @return
     */
    @RequestMapping("getLoginUserInfo")
    @ResponseBody
    public JSONObject getLoginUserInfo() {
        String logMsg = "获取当前登录用户信息";
        log.info(logMsg);
        try {
            SysUser sysUser = LoginSysUserUtil.getSysUser();
            if (sysUser == null)
                return JSONObjResult.toJSONObj("未获取到当前登录用户信息");
            SysUser result = new SysUser();
            result.setUserName(sysUser.getUserName());
            List<SysAuthority> other = new ArrayList<SysAuthority>();
            List<SysAuthority> menu = new ArrayList<SysAuthority>();
            List<SysAuthority> list = null;
            if (YesOrNo.YES.getCode().equalsIgnoreCase(sysUser.getIsAdmin()))
                list = sysAuthorityService.queryAll(null);
            else
                list = sysUserService.queryAuthority(sysUser.getId(),
                        YesOrNo.YES.getCode());
            if (list != null) {
                for (SysAuthority sysAuthority : list) {
                    if (sysAuthority.getType() == MenuOrButton.MENU.getCode())
                        menu.add(sysAuthority);
                    else
                        other.add(sysAuthority);
                }
            }

            result.setMenuArray(menu);
            result.setOtherArray(other);

            return JSONObjResult.toJSONObj(result, true, "");
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }

    }

    /**
     * 根据用户主键查找所拥有的角色
     * 
     * @param userId
     * @param enable
     * @return
     */
    @RequestMapping("queryRole")
    @ResponseBody
    public JSONObject queryRole(@RequestParam String userId, String enable) {
        String logMsg = "根据用户主键查找所拥有的角色";
        log.info(logMsg);
        try {
            if (StringUtils.isEmpty(userId))
                return JSONObjResult.toJSONObj("用户主键不能为空");
            List<SysRole> list = sysRoleService.queryByUserId(userId, enable);
            return JSONObjResult.toJSONObj(list, true, "");
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }

    }

    /**
     * 
     * 功能描述：简述功能 <br/>
     * 作 者： wdz <br/>
     * 创建时间：2016年11月3日 下午5:36:22 <br/>
     * 实现逻辑：详述实现逻辑 <br/>
     * 修 改 人： <br/>
     * 修改时间： <br/>
     * 修改说明： <br/>
     * 
     * @param pagerUtil
     * @param user
     * @return
     */
    @RequestMapping("queryUserList")
    @ResponseBody
    public JSONObject queryUserList() {
        String logMsg = "查询用户列表";
        log.info(logMsg);
        try {
            List<SysUser> list = sysUserService.findList();
            return JSONObjResult.toJSONObj(list, true, "");
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }

    }

    /**
     * 删除用户
     * 
     * @param id
     * @return
     */
    @RequestMapping("removeById")
    @ResponseBody
    public JSONObject delelteUser(@RequestParam String id) {
        String logMsg = "删除用户信息";
        log.info(logMsg);
        RestMessage result = null;
        try {
            if (StringUtils.isEmpty(id)) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }

            String logingUserId = LoginSysUserUtil.getUserId();
            if (StringUtils.isEmpty(logingUserId)) {
                return JSONObjResult.toJSONObj("未获取到登录用户信息");
            }

            result = sysUserService.deleteUsr(id, logingUserId);
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
        return JSONObjResult.toJSONObj(result);
    }

    /**
     * 保存
     * 
     * @param sysUser
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public JSONObject save(@Valid SysUser sysUser) {
        String logMsg = "保存信息";
        log.info(logMsg);
        try {
            RestMessage result;
            ValidateResult validate = ValidateUtil.validate(sysUser,
                    FormMap.getSysUserAddMap());
            if (!validate.isSuccess()) {
                return JSONObjResult.toJSONObj(validate.getErrorMsg());
            }
            String loginUserId = LoginSysUserUtil.getUserId();
            if (StringUtils.isEmpty(loginUserId)) {
                return JSONObjResult.toJSONObj("未获取到登录用户信息");
            }
            result = sysUserService.save(sysUser);
            return JSONObjResult.toJSONObj(result);
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    /**
     * 修改
     * 
     * @param
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public JSONObject update(@Valid SysUser sysUser) {
        String logMsg = "修改信息";
        log.info(logMsg);
        try {
            RestMessage result;

            ValidateResult validate = ValidateUtil.validate(sysUser,
                    FormMap.getSysUserUpdateMap());
            if (!validate.isSuccess()) {
                return JSONObjResult.toJSONObj(validate.getErrorMsg());
            }
            String loginUserId = LoginSysUserUtil.getUserId();
            if (StringUtils.isEmpty(loginUserId)) {
                return JSONObjResult.toJSONObj("未获取到登录用户信息");
            }

            if (sysUser.getId().equalsIgnoreCase(loginUserId))
                return JSONObjResult.toJSONObj("不能对当前登录人进行修改");

            result = sysUserService.update(sysUser, loginUserId);
            return JSONObjResult.toJSONObj(result);
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }

    }

    /**
     * 查询用户信息
     * 
     * @param id
     * @return
     */
    @RequestMapping("queryById")
    @ResponseBody
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "查询信息";
        log.info(logMsg);
        try {
            if (StringUtils.isEmpty(id)) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }
            SysUser user = sysUserService.findById(id);
            // 如果不存在则返回
            if (sysUserService.findById(id) == null) {
                return JSONObjResult.toJSONObj("用户不存在");
            }
            return JSONObjResult.toJSONObj(user, true, "");
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }

    }

    /**
     * 登出 , method = RequestMethod.GET
     * 
     * @return
     */
    @RequestMapping("logout")
    @ResponseBody
    public JSONObject logout(HttpServletRequest request) {
        String logMsg = "登出 ";
        log.info(logMsg);
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null && subject.getPrincipals() != null) {
                SysUser sysUser = (SysUser) subject.getPrincipal();
                String ip = IpUtil.getIpAddr(request);
                Date now = new Date();
                SysUserLoginInOut inOut = new SysUserLoginInOut();
                inOut.setInoutType(LoginInOut.OUT.getValue());
                inOut.setIp(ip);
                inOut.setRecordDate(now);
                inOut.setUserId(sysUser.getId());
                RestMessage result = sysUserLoginInOutService.save(inOut);// 保存登录日志信息
                if (result.isSuccess()) {
                    return JSONObjResult.toJSONObj(result);
                } else {
                    return JSONObjResult.toJSONObj("保存用户登录日志失败");
                }
            } else
                return JSONObjResult.toJSONObj(null, true,
                        "没有获取到用户登录信息,自动跳转到登录页");
        } catch (BusinessException e) {
            log.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }

    }
}
