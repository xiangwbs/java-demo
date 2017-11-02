package com.xwbing.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.entity.SysUserLoginInOut;
import com.xwbing.service.sys.SysUserLoginInOutService;
import com.xwbing.util.JSONObjResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 说明: 用户登录登出控制器
 * 创建日期: 2017年1月18日 下午5:57:31
 * 作者: xiangwb
 */
@RestController
@RequestMapping("/userLoginInOut/")
public class SysUserLoginInOutController {
    private final Logger logger = LoggerFactory.getLogger(SysUserLoginInOutController.class);
    @Autowired
    private SysUserLoginInOutService sysUserLoginInOutService;

    @PostMapping("queryListByPage")
    public JSONObject queryListByPage(SysUserLoginInOut userLoginInOut) {
        String logMsg = "分页查询";
        logger.info(logMsg);
        List<SysUserLoginInOut> list = sysUserLoginInOutService.queryPage(userLoginInOut);
        return JSONObjResult.toJSONObj(list, true, "");
    }
}
