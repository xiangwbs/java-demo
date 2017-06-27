package com.xwbing.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.entity.SysAuthority;
import com.xwbing.entity.vo.AuthVo;
import com.xwbing.service.sys.SysAuthorityService;
import com.xwbing.util.CommonConstant;
import com.xwbing.util.CommonEnum;
import com.xwbing.util.JSONObjResult;
import com.xwbing.util.restMessage;
import com.xwbing.validate.FormMap;
import com.xwbing.validate.ValidateResult;
import com.xwbing.validate.ValidateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 说明: 权限控制器 <br/>
 * 创建日期: 2017年1月18日 下午5:56:37 <br/>
 * 作者: xwb
 */

public class SysAuthorityController {
    private Logger log = LoggerFactory.getLogger(SysAuthorityController.class);
    @Autowired
    SysAuthorityService sysAuthorityService;

    /**
     * 删除 已经测试
     * 
     * @param id
     * @return
     */
    @RequestMapping("removeById")
    @ResponseBody
    public JSONObject removeById(@RequestParam String id) {
        String logMsg = "删除信息 id={}";
        log.info(logMsg, id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空");
        }
        // 如果不存在则返回
        if (sysAuthorityService.findById(id) == null) {
            return JSONObjResult.toJSONObj("权限不存在");
        }
        restMessage result = sysAuthorityService.deleteById(id);
        if (result.isSuccess()) {
            // 删除缓存
            // redisService.del(RedisName.authorityThree);
        }
        return JSONObjResult.toJSONObj(result);

    }

    /**
     * 查询权限详情 已经测试
     * 
     * @param id
     * @return
     */
    @RequestMapping("queryById")
    @ResponseBody
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "查询信息 id={}";
        log.info(logMsg, id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空");
        }
        // 如果不存在则返回
        if (sysAuthorityService.findById(id) == null) {
            return JSONObjResult.toJSONObj("权限不存在");
        }
        SysAuthority sysAuthority = sysAuthorityService.findById(id);
        return JSONObjResult.toJSONObj(sysAuthority, true, "");

    }

    /**
     * 保存 已经测试
     * 
     * @param guideWords
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public JSONObject save(SysAuthority sysAuthority) {
        String logMsg = "保存信息";
        log.info(logMsg);
        ValidateResult validate = ValidateUtil.validate(sysAuthority,
                FormMap.getSysAuthorityMap());
        if (!validate.isSuccess()) {
            return JSONObjResult.toJSONObj(validate.getErrorMsg());
        }
        restMessage result = sysAuthorityService.save(sysAuthority);
        if (result.isSuccess()) {
            // 删除缓存
            // redisService.del(RedisName.authorityThree);
        }
        return JSONObjResult.toJSONObj(result);
    }

    /**
     * 
     * 校验编号唯一性 已测试
     * 
     * @param update
     * @return
     */
    @RequestMapping("checkCode")
    @ResponseBody
    public JSONObject checkCode(@RequestParam String code, String id) {
        String logMsg = "校验编号唯一性";
        log.info(logMsg);
        if (StringUtils.isEmpty(code)) {
            return JSONObjResult.toJSONObj("编号不能为空");
        } else {
            boolean result = sysAuthorityService.uniqueCode(code, id);
            String error = "此用编号不能使用";
            if (result) {
                error = "";
            }
            return JSONObjResult.toJSONObj(null, result, error);
        }

    }

    /**
     * 更新
     * 
     * @param guideWords
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public JSONObject update(SysAuthority sysAuthority) {
        String logMsg = "修改信息";
        log.info(logMsg);
        if (null == sysAuthority || StringUtils.isEmpty(sysAuthority.getId())) {
            return JSONObjResult.toJSONObj("主键不能为空");
        }
        // 如果不存在则返回
        if (sysAuthorityService.findById(sysAuthority.getId()) == null) {
            return JSONObjResult.toJSONObj("权限不存在");
        }

        ValidateResult validate = ValidateUtil.validate(sysAuthority,
                FormMap.getSysAuthorityMap());
        if (!validate.isSuccess()) {
            return JSONObjResult.toJSONObj(validate.getErrorMsg());
        }

        String isEnable = sysAuthority.getIsEnable();
        // 如果禁用，查询是否有子节点，如果有，子节点也要被禁用
        if (CommonConstant.ISNOTENABLE.equals(isEnable)) {
            // 子节点的权限都禁用
            if (!sysAuthorityService.disableChildrenByParentId(sysAuthority
                    .getId())) {
                return JSONObjResult.toJSONObj("禁用子节点权限失败");
            }

        } else {
            // 如果是启用看父节点是否被禁用,一级的不需要判断
            if (!CommonConstant.ROOT.equals(sysAuthority.getParentId())) {
                SysAuthority queryOne = sysAuthorityService
                        .findById(sysAuthority.getParentId());
                if (null != queryOne) {
                    if (CommonEnum.YesOrNo.NO.getCode().equals(
                            queryOne.getIsEnable())) {
                        return JSONObjResult.toJSONObj("父节点已被禁用，请先启用父节点");
                    }
                } else {
                    return JSONObjResult.toJSONObj("父节点异常");
                }
            }
        }
        restMessage result = sysAuthorityService.update(sysAuthority);
        return JSONObjResult.toJSONObj(result);
    }

    /**
     * 根据父节点查询子节点（非递归）
     * 
     * @param parentId
     * @return
     */
    @RequestMapping("queryByParentId")
    @ResponseBody
    public JSONObject queryByParentId(String parentId) {
        String logMsg = "根据父节点查询";
        log.info(logMsg);
        if (StringUtils.isEmpty(parentId)) {
            parentId = CommonConstant.ROOT;
        }
        // 如果不存在则返回
        if (!CommonConstant.ROOT.equals(parentId)
                && sysAuthorityService.deleteById(parentId) == null) {
            return JSONObjResult.toJSONObj("父节点不存在");
        }
        List<SysAuthority> queryByParentId = sysAuthorityService
                .queryByParentId(parentId, null);
        return JSONObjResult.toJSONObj(queryByParentId, true, "");
    }

    /**
     * 根据是否启用查询所有权限
     * 
     * @param enable
     * @return
     */
    @RequestMapping("queryAllAuthByEnable")
    @ResponseBody
    public JSONObject queryAllAuthorityByEnable(@RequestParam String enable) {
        String logMsg = "查询所有可用角色";
        log.info(logMsg);
        List<SysAuthority> authoritys = sysAuthorityService.queryAll(enable);
        return JSONObjResult.toJSONObj(authoritys, true, "");
    }

    /**
     * 递归查询所有角色
     * 
     * @param parentId
     * @param enable
     * @return
     */
    @RequestMapping("queryTree")
    @ResponseBody
    public JSONObject queryTree(String enable) {
        String logMsg = "查询所有可用角色";
        log.info(logMsg);
        List<AuthVo> authoritys = null;
        // boolean tag = redisService.exists(RedisName.authorityThree);
        // if (tag) {
        // String result = redisService.get(RedisName.authorityThree);
        // authoritys = JSONArray.parseArray(result, SysAuthVo.class);
        // }
        if (authoritys == null || authoritys.size() == 0) {
            authoritys = sysAuthorityService.queryAllChildren(
                    CommonConstant.ROOT, enable);
            // 设置缓存
            // redisService.set(RedisName.authorityThree,
            // JSONArray.toJSONString(authoritys));
        }

        return JSONObjResult.toJSONObj(authoritys, true, "");
    }

}
