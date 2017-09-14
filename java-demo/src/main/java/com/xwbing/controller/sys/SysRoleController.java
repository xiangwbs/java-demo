package com.xwbing.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.entity.SysAuthority;
import com.xwbing.entity.SysRole;
import com.xwbing.entity.SysRoleAuthority;
import com.xwbing.service.sys.SysAuthorityService;
import com.xwbing.service.sys.SysRoleAuthorityService;
import com.xwbing.service.sys.SysRoleService;
import com.xwbing.util.JSONObjResult;
import com.xwbing.util.RestMessage;
import com.xwbing.validate.FormMap;
import com.xwbing.validate.ValidateResult;
import com.xwbing.validate.ValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明: 角色控制器
 * 创建日期: 2017年1月18日 下午5:56:58
 * 作者: xiangwb
 */
@Api("角色信息")
@RestController
@RequestMapping("/role/")
public class SysRoleController {
    private Logger log = LoggerFactory.getLogger(SysRoleController.class);
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysAuthorityService sysAuthorityService;
    @Autowired
    SysRoleAuthorityService sysRoleAuthorityService;

    /**
     * 删除 已测试
     * 
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "根据主键删除角色信息")
    @ApiImplicitParam(name = "id", value = "角色主键，长度1-50", paramType = "query", required = true, dataType = "string")
    @PostMapping("removeById")
    public JSONObject removeById(@RequestParam String id) {
        String logMsg = "删除信息 id={}";
        log.info(logMsg, id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空");
        }
        SysRole role = sysRoleService.findById(id);
        // 如果不存在则返回
        if (role == null) {
            return JSONObjResult.toJSONObj("角色不存在");
        }
        RestMessage result = sysRoleService.deleteById(id);
        return JSONObjResult.toJSONObj(result);

    }

    /**
     * 查询角色 已测试
     * 
     * @param id
     * @return
     */
    @ApiOperation(value = "查询", notes = "根据主键查询角色信息")
    @ApiImplicitParam(name = "id", value = "角色主键，长度1-50", paramType = "query", required = true, dataType = "string")
    @PostMapping("queryById")
    public JSONObject queryById(@RequestParam String id) {
        String logMsg = "查询信息 id={}";
        log.info(logMsg, id);
        if (StringUtils.isEmpty(id)) {
            return JSONObjResult.toJSONObj("主键不能为空");
        }
        SysRole sysAuthority = sysRoleService.findById(id);
        // 如果不存在则返回
        if (sysAuthority == null) {
            return JSONObjResult.toJSONObj("角色不存在");
        }
        return JSONObjResult.toJSONObj(sysAuthority, true, "");

    }

    /**
     * 保存 已测试
     * 
     * @param sysRole
     * @return
     */
    @ApiOperation(value = "保存", notes = "保存角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称,长度1-50", required=true,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "code", value = "编码,长度1-50", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "isEnable", value = "是否启用,Y|N", required=true,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "remark", value = "描述,长度1-50",  paramType = "query", dataType = "string")
    })
    @PostMapping("save")
    public JSONObject save(SysRole sysRole) {
        String logMsg = "保存信息";
        log.info(logMsg);
        ValidateResult validate = ValidateUtil.validate(sysRole,
                FormMap.getSysRoleMap());
        if (!validate.isSuccess()) {
            return JSONObjResult.toJSONObj(validate.getErrorMsg());
        }
        RestMessage result = sysRoleService.save(sysRole);
        return JSONObjResult.toJSONObj(result);
    }

    /**
     * 更新
     * 
     * @param sysRole
     * @return
     */
    @PostMapping("update")
    public JSONObject update(SysRole sysRole) {
        String logMsg = "修改信息";
        log.info(logMsg);
        if (StringUtils.isEmpty(sysRole.getId())) {
            return JSONObjResult.toJSONObj("主键不能为空");
        }
        SysRole role = sysRoleService.findById(sysRole.getId());
        // 如果不存在则返回
        if (role == null) {
            return JSONObjResult.toJSONObj("角色不存在");
        }

        ValidateResult validate = ValidateUtil.validate(sysRole,
                FormMap.getSysRoleMap());
        if (!validate.isSuccess()) {
            return JSONObjResult.toJSONObj(validate.getErrorMsg());
        }
        RestMessage result = sysRoleService.update(sysRole);
        return JSONObjResult.toJSONObj(result);
    }

    /**
     * 
     * 校验编号唯一性 已测试
     * 
     * @param
     * @return
     */
    @PostMapping("checkCode")
    public JSONObject checkCode(@RequestParam String code, String id) {
        String logMsg = "校验编号唯一性";
        log.info(logMsg);
        if (StringUtils.isEmpty(code))
            return JSONObjResult.toJSONObj("编号不能为空");
        boolean result = sysRoleService.uniqueCode(code, id);
        String error = "此用编号不能使用";
        if (result) {
            error = "";
        }
        return JSONObjResult.toJSONObj(null, result, error);
    }

    /**
     * 根据角色主键查找权限
     * 
     * @param roleId
     * @return
     */
    @PostMapping("queryAuthorityByRoleId")
    public JSONObject queryAuthorityByRoleId(@RequestParam String roleId,
            String enable) {
        String logMsg = "根据角色主键查找权限";
        log.info(logMsg);
        if (StringUtils.isEmpty(roleId)) {
            return JSONObjResult.toJSONObj("角色主键不能为空");
        }
        SysRole role = sysRoleService.findById(roleId);
        // 如果不存在则返回
        if (role == null) {
            return JSONObjResult.toJSONObj("角色不存在");
        }
        List<SysAuthority> rAuthoritys = null;
        rAuthoritys = sysAuthorityService.queryByRoleId(roleId, enable);

        return JSONObjResult.toJSONObj(rAuthoritys, true, "");

    }

    /**
     * 保存角色 权限主键
     * 
     * @param authoritys
     * @param roleId
     * @return
     */
    @PostMapping("saveAuthority")
    public JSONObject saveAuthority(@RequestParam String authoritys,
            @RequestParam String roleId) {
        String logMsg = "权限主键集合 ids={}";
        log.info(logMsg, authoritys);
        if (StringUtils.isEmpty(authoritys))
            return JSONObjResult.toJSONObj("权限主键集合不能为空");
        if (StringUtils.isEmpty(roleId))
            return JSONObjResult.toJSONObj("角色主键不能为空");
        SysRole role = sysRoleService.findById(roleId);
        // 如果不存在则返回
        if (role == null) {
            return JSONObjResult.toJSONObj("角色不存在");
        }
        String ids[] = authoritys.split(",");
        List<SysRoleAuthority> list = new ArrayList<SysRoleAuthority>();
        for (String id : ids) {
            SysRoleAuthority re = new SysRoleAuthority();
            re.setRoleId(roleId);
            re.setAuthorityId(id);
            list.add(re);
        }
        RestMessage result = sysRoleAuthorityService.saveBatch(list, roleId);
        return JSONObjResult.toJSONObj(result);
    }

    @PostMapping("queryAllRoleByEnable")
    public JSONObject queryAllRoleByEnable(SysRole role) {
        String logMsg = "查询所有可用角色";
        log.info(logMsg);
        List<SysRole> roles = sysRoleService.findShowListByPage(role);
        return JSONObjResult.toJSONObj(roles, true, "");
    }

}
