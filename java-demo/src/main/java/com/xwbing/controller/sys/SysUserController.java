package com.xwbing.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.Exception.BusinessException;
import com.xwbing.Exception.InvalidPassWordException;
import com.xwbing.entity.*;
import com.xwbing.service.sys.*;
import com.xwbing.shiro.UsernamePasswordCaptchaToken;
import com.xwbing.util.*;
import com.xwbing.util.CommonEnum.LoginInOutEnum;
import com.xwbing.util.CommonEnum.MenuOrButtonEnum;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 说明: 用户控制器
 * 创建日期: 2017年1月18日 下午5:57:18
 * 作者: xiangwb
 */
@RestController
@RequestMapping("/user/")
public class SysUserController {
    private final Logger logger = LoggerFactory.getLogger(SysUserController.class);
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysUserLoginInOutService sysUserLoginInOutService;
    @Autowired
    private SysAuthorityService sysAuthorityService;

    @PostMapping("loginCheck")
    public JSONObject loginCheck(SysUser sysUser, boolean rememberMe, @RequestParam String captcha, HttpServletRequest request) {
        String logMsg = "loginCheck";
        logger.info(logMsg + "start");
        try {
            // 先判断是不是为空
            if (StringUtils.isEmpty(sysUser.getUserName()) || StringUtils.isEmpty(sysUser.getPassword())) {
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
                RestMessage result = sysUserService.updateLoginInfo(userId, ip, now);// 修改最后登录信息
                if (result.isSuccess()) {
                    SysUserLoginInOut inOut = new SysUserLoginInOut();
                    inOut.setInoutType(LoginInOutEnum.IN.getCode());
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
    @PostMapping("updatePassWord")
    public JSONObject updatePassWord(@RequestParam String oldPassWord, @RequestParam String newPassWord) {
        String logMsg = "当前登录用户修改密码";
        logger.info(logMsg);
        try {
            String loginUserId = LoginSysUserUtil.getUserId();
            if (StringUtils.isEmpty(loginUserId))
                return JSONObjResult.toJSONObj("未能获取到登录信息，请重新登录");
            RestMessage result = sysUserService.updatePassWord(newPassWord, oldPassWord, loginUserId);
            return JSONObjResult.toJSONObj(result);
        } catch (BusinessException e) {
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    /**
     * 保存用户角色
     *
     * @return
     */
    @PostMapping("saveRole")
    public JSONObject saveRole(@RequestParam String roleIds, @RequestParam String userId) {
        String logMsg = "保存用户角色";
        logger.info(logMsg + "ids={}", roleIds);
        try {
            if (StringUtils.isEmpty(roleIds))
                return JSONObjResult.toJSONObj("角色主键不能为空");
            if (StringUtils.isEmpty(userId))
                return JSONObjResult.toJSONObj("用户主键不能为空");
            SysUser old = sysUserService.findById(userId);
            if (old == null) {
                return JSONObjResult.toJSONObj("没有查询到对象");
            }
            if (CommonEnum.YesOrNoEnum.YES.getCode().equalsIgnoreCase(old.getIsAdmin())) {
                return JSONObjResult.toJSONObj("不能对管理员进行删除操作");
            }
            String ids[] = roleIds.split(",");
            List<SysUserRole> list = new ArrayList<>();
            for (String id : ids) {
                SysUserRole re = new SysUserRole();
                re.setRoleId(id);
                re.setUserId(userId);
                list.add(re);
            }
            RestMessage result = sysUserRoleService.saveBatch(list, userId);
            return JSONObjResult.toJSONObj(result);
        } catch (BusinessException e) {
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
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
    @PostMapping("queryAuthority")
    public JSONObject queryAuthority(@RequestParam String userId, @RequestParam String enable) {
        String logMsg = "根据用户主键查找所拥有的权限";
        logger.info(logMsg);
        if (StringUtils.isEmpty(userId))
            return JSONObjResult.toJSONObj("用户主键不能为空");
        try {
            List<SysAuthority> list = sysUserService.queryAuthority(userId, enable);
            return JSONObjResult.toJSONObj(list, true, "");
        } catch (BusinessException e) {
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @GetMapping("getLoginUserInfo")
    public JSONObject getLoginUserInfo() {
        String logMsg = "获取当前登录用户信息";
        logger.info(logMsg);
        try {
            SysUser sysUser = LoginSysUserUtil.getSysUser();
            if (sysUser == null)
                return JSONObjResult.toJSONObj("未获取到当前登录用户信息");
            SysUser result = new SysUser();
            result.setUserName(sysUser.getUserName());
            List<SysAuthority> other = new ArrayList<>();
            List<SysAuthority> menu = new ArrayList<>();
            List<SysAuthority> list;
            if (CommonEnum.YesOrNoEnum.YES.getCode().equalsIgnoreCase(sysUser.getIsAdmin()))
                list = sysAuthorityService.queryAll(null);
            else
                list = sysUserService.queryAuthority(sysUser.getId(), CommonEnum.YesOrNoEnum.YES.getCode());
            if (list != null) {
                for (SysAuthority sysAuthority : list) {
                    if (sysAuthority.getType() == MenuOrButtonEnum.MENU.getCode())
                        menu.add(sysAuthority);
                    else
                        other.add(sysAuthority);
                }
            }
            result.setMenuArray(menu);
            result.setOtherArray(other);
            return JSONObjResult.toJSONObj(result, true, "");
        } catch (BusinessException e) {
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
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
    @PostMapping("queryRole")
    public JSONObject queryRole(@RequestParam String userId, String enable) {
        String logMsg = "根据用户主键查找所拥有的角色";
        logger.info(logMsg);
        try {
            if (StringUtils.isEmpty(userId))
                return JSONObjResult.toJSONObj("用户主键不能为空");
            List<SysRole> list = sysRoleService.queryByUserId(userId, enable);
            return JSONObjResult.toJSONObj(list, true, "");
        } catch (BusinessException e) {
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    @GetMapping("queryUserList")
    public JSONObject queryUserList() {
        String logMsg = "查询用户列表";
        logger.info(logMsg);
        try {
            List<SysUser> list = sysUserService.findList();
            return JSONObjResult.toJSONObj(list, true, "");
        } catch (BusinessException e) {
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @GetMapping("removeById")
    public JSONObject delelteUser(@RequestParam String id) {
        String logMsg = "删除用户信息";
        logger.info(logMsg);
        RestMessage result;
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
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
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
    @PostMapping("save")
    public JSONObject save(@Valid SysUser sysUser) {
        String logMsg = "保存信息";
        logger.info(logMsg);
        try {
            RestMessage result;
            ValidateResult validate = ValidateUtil.validate(sysUser, FormMap.getSysUserAddMap());
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
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    /**
     * 修改
     *
     * @param sysUser
     * @return
     */
    @PostMapping("update")
    public JSONObject update(@Valid SysUser sysUser) {
        String logMsg = "修改信息";
        logger.info(logMsg);
        try {
            RestMessage result;
            ValidateResult validate = ValidateUtil.validate(sysUser, FormMap.getSysUserUpdateMap());
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
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    /**
     * 查询用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("queryById")
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "查询信息";
        logger.info(logMsg);
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
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }

    /**
     * 登出 , method = RequestMethod.GET
     *
     * @return
     */
    @GetMapping("logout")
    public JSONObject logout(HttpServletRequest request) {
        String logMsg = "登出 ";
        logger.info(logMsg);
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null && subject.getPrincipals() != null) {
                SysUser sysUser = (SysUser) subject.getPrincipal();
                String ip = IpUtil.getIpAddr(request);
                Date now = new Date();
                SysUserLoginInOut inOut = new SysUserLoginInOut();
                inOut.setInoutType(LoginInOutEnum.OUT.getCode());
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
                return JSONObjResult.toJSONObj(null, true, "没有获取到用户登录信息,自动跳转到登录页");
        } catch (BusinessException e) {
            logger.error(logMsg + "===" + e.getMessage());
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logMsg + "===异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员 " + e.getMessage());
        }
    }
}
