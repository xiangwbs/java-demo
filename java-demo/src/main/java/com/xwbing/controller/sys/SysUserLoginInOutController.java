package com.xwbing.controller.sys;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.entity.SysUserLoginInOut;
import com.xwbing.service.sys.SysUserLoginInOutService;
import com.xwbing.util.JSONObjResult;

/**
 * 说明: 用户登录登出控制器 <br/>
 * 创建日期: 2017年1月18日 下午5:57:31 <br/>
 * 作者: xwb
 */
@Controller
@RequestMapping("/userLoginInOut/")
public class SysUserLoginInOutController {
    private Logger log = LoggerFactory
            .getLogger(SysUserLoginInOutController.class);
    @Autowired
    SysUserLoginInOutService sysUserLoginInOutService;

    @RequestMapping("queryListByPage")
    @ResponseBody
    public JSONObject queryListByPage(SysUserLoginInOut userLoginInOut) {
        String logMsg = "分页查询";
        log.info(logMsg);
        List<SysUserLoginInOut> list = sysUserLoginInOutService
                .queryPage(userLoginInOut);
        return JSONObjResult.toJSONObj(list, true, "");
    }
}
