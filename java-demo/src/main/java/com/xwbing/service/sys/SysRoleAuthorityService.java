package com.xwbing.service.sys;

import com.xwbing.Exception.BusinessException;
import com.xwbing.dao.SysRoleAuthorityDao;
import com.xwbing.entity.SysRoleAuthority;
import com.xwbing.util.LoginSysUserUtil;
import com.xwbing.util.RestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明:
 * 项目名称: zdemo
 * 创建日期: 2017年1月18日 下午5:47:53
 * 作者: xiangwb
 */
@Service
public class SysRoleAuthorityService {
    @Autowired
    private SysRoleAuthorityDao sysRoleAuthorityDao;

    /**
     * 执行用户角色权限保存操作,保存之前先判断是否存在，存在删除
     *
     * @param list
     * @param roleId
     * @return
     */
    public RestMessage saveBatch(List<SysRoleAuthority> list, String roleId) {
        RestMessage result = new RestMessage();
        Map<String, Object> params = new HashMap<>();
        params.put("role_id", roleId);
        List<SysRoleAuthority> exits = sysRoleAuthorityDao.select(params);
        if (exits != null && exits.size() > 0) {
            String id;
            Map<String, Object> map = new HashMap<>();
            for (SysRoleAuthority temp : exits) {
                id = temp.getId();
                map.put("id", id);
                int row = sysRoleAuthorityDao.delete(params);
                if (row == 0) {
                    throw new BusinessException("删除角色权限关联信息错误");
                }
            }
        }
        for (SysRoleAuthority sysRoleAuthority : list) {
            sysRoleAuthority.setCreator(LoginSysUserUtil.getUserId());
            int row = sysRoleAuthorityDao.update(sysRoleAuthority);
            if (row == 0) {
                throw new BusinessException("批量保存数据失败");
            }
        }
        result.setMsg("批量保存成功");
        result.setSuccess(true);
        return result;
    }

    /**
     * 根据角色主键查找
     *
     * @param roleId
     * @return
     */
    public List<SysRoleAuthority> queryByRoleId(String roleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("role_id", roleId);
        return sysRoleAuthorityDao.select(params);
    }
}
