package com.xwbing.shiro;

import com.xwbing.entity.SysAuthority;
import com.xwbing.entity.SysRole;
import com.xwbing.entity.SysUser;
import com.xwbing.service.sys.SysAuthorityService;
import com.xwbing.service.sys.SysRoleService;
import com.xwbing.service.sys.SysUserService;
import com.xwbing.util.CommonEnum;
import com.xwbing.util.Digests;
import com.xwbing.util.EncodeUtils;
import com.xwbing.util.captcah.CaptchaException;
import com.xwbing.util.captcah.CaptchaServlet;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 说明:鉴权使用的
 * 创建日期: 2016年7月13日 上午9:37:09
 * 作者: xiangwb
 */
@Component
public class MyShiroRelam extends AuthorizingRealm {

    private SysAuthorityService sysAuthorityService;

    private SysUserService sysUserService;

    private SysRoleService sysRoleService;

    public void setSysAuthorityService(SysAuthorityService sysAuthorityService) {
        this.sysAuthorityService = sysAuthorityService;
    }

    public void setSysUserService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    public void setSysRoleService(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    /**
     * 获取身份信息,从数据库获取该用户的权限和角色信息
     * 
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法
     * 此处只有数据库配置了菜单访问的时候才验证，否则不验证
     */

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principalCollection) {

        // 获取验证的对象
        if (principalCollection == null) {
            throw new AuthorizationException("Principal对象不能为空");
        }

        SysUser sysUser = (SysUser) principalCollection.fromRealm(getName())
                .iterator().next();
        // 到数据库查是否有此对象
        if (sysUser != null) {
            // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            String userId = sysUser.getId();
            String isAdmin = sysUser.getIsAdmin();

            List<SysRole> listRole = null;
            // 如果是管理员
            if (CommonEnum.YesOrNoEnum.YES.getCode().equalsIgnoreCase(isAdmin))
                listRole = sysRoleService.queryAll(CommonEnum.YesOrNoEnum.YES
                        .getCode());
            else
                listRole = sysRoleService.queryByUserId(userId,
                        CommonEnum.YesOrNoEnum.YES.getCode());
            // 用户的角色集合
            Set<String> roles = new HashSet<String>();
            if (listRole != null) {
                for (SysRole sysRole : listRole) {
                    roles.add(sysRole.getCode());// 获取编号
                }
            }
            info.setRoles(roles);
            System.out.println("拥有的角色==" + roles);
            // // 用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要

            // 获取用户对应的权限
            Set<String> permissions = new HashSet<String>();
            List<SysAuthority> listAuthoritys = new ArrayList<SysAuthority>();
            if (CommonEnum.YesOrNoEnum.YES.getCode().equalsIgnoreCase(isAdmin)) {// 超级管理员
                listAuthoritys = sysAuthorityService
                        .queryAll(CommonEnum.YesOrNoEnum.YES.getCode());
            } else {
                if (listRole != null) {// 角色不为空
                    for (SysRole sysRole : listRole) {
                        List<SysAuthority> temp = sysAuthorityService
                                .queryByRoleId(sysRole.getId(),
                                        CommonEnum.YesOrNoEnum.YES.getCode());
                        if (temp != null)
                            listAuthoritys.addAll(temp);
                    }

                }
            }
            if (listAuthoritys != null)
                for (SysAuthority sysAuthority : listAuthoritys) {
                    permissions.add(sysAuthority.getUrl());// 此系统暂时没有用角色名称去区分登录信息,故此处name没有处理
                }
            System.out.println("拥有的权限==" + permissions);
            info.setStringPermissions(permissions);

            return info;
        }
        return null;

    }

    /**
     * 登录的时候调用 , 进行鉴权但缓存中无用户的授权信息时调用. //登录信息和用户验证信息验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authenticationToken;

        // 增加判断验证码逻辑
        String captcha = token.getCaptcha();
        String exitCode = (String) SecurityUtils.getSubject().getSession()
                .getAttribute(CaptchaServlet.KEY_CAPTCHA);
        if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
            throw new CaptchaException("验证码错误");
        }
        // 查出是否有此用户
        SysUser sysUser = null;
        sysUser = sysUserService.queryUserName(token.getUsername());

        if (sysUser == null) {
            throw new UnknownAccountException("账号或者密码不正确");// 没找到帐号
        }

        // 获取密码盐值 系统存的
        String pswdSalt = sysUser.getSalt();
        // 用户填写的登录密码
        String passWord = String.valueOf(token.getPassword());
        // 根据密码盐值， 解码
        byte[] salt = EncodeUtils.hexDecode(pswdSalt);
        byte[] hashPassword = Digests.sha1(passWord.getBytes(), salt,
                SysUser.HASH_INTERATIONS);
        // 密码 数据库中密码
        String validatePassWord = EncodeUtils.hexEncode(hashPassword);
        // 获取存在系统中的密码
        String sysPassWord = sysUser.getPassword();
        if (validatePassWord.equals(sysPassWord)) {// 如果密码正确
            // 增加密码失效判断时间
//            if (YesOrNoEnum.YES.getCode().equalsIgnoreCase(
//                    sysUser.getIsResetPassword())) {// 如果是重置密码，
//                Date invalidTime = sysUser.getPasswordInvalidTime();// 获取密码失效时间
//                if (invalidTime.getTime() < (new Date()).getTime()) {
//                    throw new InvalidPassWordException("密码已经失效");
//                }
//            }
            // 判断是否锁定
            // String isLock = sysUser.getIsLock();
            // if (CommonEnum.YesOrNoEnum.YES.getCode().equalsIgnoreCase(isLock))
            // {// 如果为0表示锁定
            // throw new LockedAccountException("账号被锁定");// 没找到帐号
            // } else {
            // 重新把验证通过的密码放置到token中
            char[] st = validatePassWord.toCharArray();
            token.setPassword(st);
            return new SimpleAuthenticationInfo(sysUser, sysPassWord,
                    ByteSource.Util.bytes(sysUser.getSalt()), getName());

        } else
            throw new UnknownAccountException("账号或者密码不正确");// 没找到帐号

    }

}
