package com.xwbing.service.sys;

import com.xwbing.entity.SysUser;
import com.xwbing.entity.SysUserLoginInOut;
import com.xwbing.util.CommonEnum;
import com.xwbing.util.RestMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 说明:
 * 项目名称: zdemo
 * 创建日期: 2017年1月18日 下午5:48:34
 * 作者: xiangwb
 */
@Service
public class SysUserLoginInOutService {
    @Autowired
    private SysUserService sysUserService;

    public RestMessage save(SysUserLoginInOut resortsUserLoginInOut) {
        return null;
    }

    public SysUserLoginInOut findById(String id) {
        return null;
    }

    /**
     * 分页查询用户登录登出信息
     *
     * @param userLoginInOut
     * @return
     */
    public List<SysUserLoginInOut> queryPage(SysUserLoginInOut userLoginInOut) {
        StringBuilder sql = new StringBuilder().append("select * from ");
        sql.append(SysUserLoginInOut.table + " s," + SysUser.table + " u");
        sql.append(" where s.user_id=u.id and u.is_deleted = 'N' ");
        // 用户名称
        if (StringUtils.isNotEmpty(userLoginInOut.getUserIdName())) {
            sql.append(" and u.user_name like '%"
                    + userLoginInOut.getUserIdName() + "%' ");
        }
        int inOutType = userLoginInOut.getInoutType();
        // 判断登录登出
        if (inOutType == CommonEnum.LoginInOutEnum.IN.getCode()
                || inOutType == CommonEnum.LoginInOutEnum.OUT.getCode()) {
            sql.append(" and s.inout_type = " + inOutType);
        }
        // 用户ip
        if (StringUtils.isNotEmpty(userLoginInOut.getIp())) {
            sql.append(" and s.ip like '%" + userLoginInOut.getIp() + "%'");
        }
        // 开始时间
        if (StringUtils.isNotEmpty(userLoginInOut.getStartDate())) {
            sql.append(" and s.record_date>='" + userLoginInOut.getStartDate()
                    + "'");
        }
        // 结束时间
        if (StringUtils.isNotEmpty(userLoginInOut.getEndDate())) {
            sql.append(" and s.record_date<='" + userLoginInOut.getEndDate()
                    + "'");
        }
        return null;
    }
}
