package com.xwbing.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.Exception.BusinessException;
import com.xwbing.dao.SysConfigDao;
import com.xwbing.entity.SysConfig;
import com.xwbing.util.CommonEnum;
import com.xwbing.util.LoginSysUserUtil;
import com.xwbing.util.RestMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明: 系统配置服务
 * 创建日期: 2017年3月7日 下午2:04:31
 * 作者: xiangwb
 */
@Service
public class SysConfigService {
    @Autowired
    private SysConfigDao sysConfigDao;

    /**
     * 新增系统配置
     *
     * @param data
     * @return
     */
    public RestMessage addConfig(SysConfig data) {
        RestMessage result = new RestMessage();
        if (null == data) {
            throw new BusinessException("配置数据不能为空");
        }
        // 检查当前系统配置
        SysConfig sysConfig = findByCode(data.getCode());
        if (null != sysConfig && CommonEnum.YesOrNoEnum.NO.getCode().equals(sysConfig.getIsDeleted())) {
            throw new BusinessException(data.getCode() + "已存在");
        }
        data.setIsDeleted("N");
        data.setModifier(LoginSysUserUtil.getUserId());
        int row = sysConfigDao.save(data);
        if (row == 1) {
            result.setMsg("保存配置成功");
            result.setSuccess(true);
        } else {
            result.setMsg("保存配置失败");
        }
        return result;
    }

    /**
     * 删除系统配置
     *
     * @param code
     * @return
     */
    public RestMessage removeConfigByCode(String code) {
        RestMessage result = new RestMessage();
        if (StringUtils.isEmpty(code)) {
            throw new BusinessException(" code不能为空");
        }
        // 检查当前系统配置
        SysConfig sysConfig = findByCode(code);
        if (null == sysConfig) {
            throw new BusinessException("已删除");
        }
        int row = sysConfigDao.delete(sysConfig.getId());
        if (row == 1) {
            result.setMsg("删除配置成功");
            result.setSuccess(true);
        } else {
            result.setMsg("删除配置失败");
        }
        return result;
    }

    /**
     * 更新
     *
     * @param code
     * @param value
     * @return
     */
    public RestMessage update(String code, String value) {
        RestMessage result = new RestMessage();
        SysConfig systemConfig = findByCode(code);
        if (systemConfig == null) {
            throw new BusinessException(" 查找不到对象");
        }
        systemConfig.setValue(value);
        systemConfig.setModifier(LoginSysUserUtil.getUserId());
        int row = sysConfigDao.update(systemConfig);
        if (row == 1) {
            result.setMsg("更新配置成功");
            result.setSuccess(true);
        } else {
            result.setMsg("更新配置失败");
        }
        return result;
    }

    /**
     * 更新
     *
     * @param systemConfig
     * @return
     */
    public RestMessage update(SysConfig systemConfig) {
        RestMessage result = new RestMessage();
        if (systemConfig == null) {
            throw new BusinessException(" 查找不到对象");
        }
        int row = sysConfigDao.update(systemConfig);
        if (row == 1) {
            result.setMsg("更新配置成功");
            result.setSuccess(true);
        } else {
            result.setMsg("更新配置失败");
        }
        return result;
    }

    /**
     * 根据Code查询
     *
     * @param code
     * @return
     */
    public SysConfig findByCode(String code) {
        Map<String, Object> term = new HashMap<>();
        term.put("code", code);
        term.put("is_deleted", CommonEnum.YesOrNoEnum.NO.getCode());
        return sysConfigDao.findOne(term);
    }

    /**
     * 根据Code查询查询相应配置模型
     *
     * @param code
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getSysConfigByKeyword(String code, Class<T> clazz) {
        SysConfig sysConfigByKeyword = this.findByCode(code);
        if (null != sysConfigByKeyword) {
            String value = sysConfigByKeyword.getValue();
            if (StringUtils.isNotEmpty(value)) {
                return JSONObject.parseObject(value, clazz);
            }
        }
        return null;
    }
}
