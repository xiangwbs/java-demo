package com.xwbing.service.sys;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwbing.entity.SysUser;
import com.xwbing.entity.SysUserLoginInOut;
import com.xwbing.util.CommonEnum;
import com.xwbing.util.ResultMessage;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zdemo <br/>
 * 创建日期: 2017年1月18日 下午5:48:34 <br/>
 * 作者: xwb
 */
@Service
public class SysUserLoginInOutService {
    @Autowired
    private SysUserService sysUserService;

    public ResultMessage save(SysUserLoginInOut resortsUserLoginInOut) {
        return null;
    }

    public SysUserLoginInOut findById(String id) {
        return null;
    }

    /**
     * 
     * 功能描述： 分页查询用户登录登出信息 <br/>
     * 作 者：xwb <br/>
     * 创建时间：2017年3月2日 上午11:22:28 <br/>
     * 
     * @param pageNo
     * @param pageSize
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
        if (inOutType == CommonEnum.LoginInOut.IN.getValue()
                || inOutType == CommonEnum.LoginInOut.OUT.getValue()) {
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
