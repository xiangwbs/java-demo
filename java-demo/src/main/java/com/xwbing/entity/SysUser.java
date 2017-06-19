package com.xwbing.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 说明:用户 <br/>
 * 表单验证validator  新增和修改时 cotroller层  参数bean前要加@Valid注解
 * 需要导入hibernate-validator.jar
 * 创建日期: 2016年12月9日 下午4:10:47 <br/>
 * 作者: xwb
 */
public class SysUser implements Serializable {
    private static final long serialVersionUID = -3343404252507060640L;
    public static String table = "sys_user_info";
    /**
     * 密码盐长度
     */
    public static final int SALT_SIZE = 10;
    /**
     * 初始默认密码长度
     */
    public static final int PWD_LENGTH = 8;
    /**
     * hash
     */
    public static int HASH_INTERATIONS = 1024;

    private String id;
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 6, max = 50, message = "长度为6-50")
    @Pattern(regexp = "[a-zA-Z0-9_]", message = "用户名需在6-50个字符范围内,为数字字母下划线组合")
    private String userName;
    /**
     * 姓名
     */
    @NotEmpty(message = "姓名不能为空")
    @Length(min = 1, max = 10, message = "长度为1-10")
    private String name;
    /**
     * 盐值
     */
    private String salt;
    /**
     * 密码
     */
    private String password;
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 邮箱
     */
    private String mail;
    private String modifier;
    private String creator;
    /**
     * 性别
     */
    @NotEmpty(message = "性别不能为空")
    @Pattern(regexp = "[01]", message = "性别格式为0|1,0代表女,1代表男")
    private int sex;
    /**
     * 是否管理员
     */
    @NotEmpty(message = "是否管理员不能为空")
    @Pattern(regexp = "Y|N", message = "是否管理员格式为Y|N")
    private String isAdmin;
    @JSONField(serialize = false)
    List<SysAuthority> menuArray;
    @JSONField(serialize = false)
    List<SysAuthority> otherArray;
    /**
     * 模糊查询条件
     */
    @JSONField(serialize = false)
    private String condition;

    public List<SysAuthority> getMenuArray() {
        return menuArray;
    }

    public void setMenuArray(List<SysAuthority> menuArray) {
        this.menuArray = menuArray;
    }

    public List<SysAuthority> getOtherArray() {
        return otherArray;
    }

    public void setOtherArray(List<SysAuthority> otherArray) {
        this.otherArray = otherArray;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
