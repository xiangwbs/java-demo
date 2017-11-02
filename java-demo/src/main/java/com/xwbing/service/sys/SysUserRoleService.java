package com.xwbing.service.sys;

import com.xwbing.Exception.BusinessException;
import com.xwbing.dao.SysUserRoleDao;
import com.xwbing.entity.SysUserRole;
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
 * 创建日期: 2017年1月18日 下午5:48:10
 * 作者: xiangwb
 */
@Service
public class SysUserRoleService {
    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    public RestMessage deleteById(String id) {
        RestMessage result = new RestMessage();
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        int row = sysUserRoleDao.delete(params);
        if (row == 1) {
            result.setMsg("删除用户角色关联成功");
            result.setSuccess(true);
        } else {
            result.setMsg("删除用户角色关联失败");
        }
        return result;
    }

    /**
     * 执行用户角色权限保存操作,保存之前先判断是否存在，存在删除
     *
     * @param list
     * @param userId
     * @return
     */
    public RestMessage saveBatch(List<SysUserRole> list, String userId) {
        RestMessage result = new RestMessage();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        List<SysUserRole> exits = sysUserRoleDao.select(params);
        if (exits != null && exits.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            String id;
            for (SysUserRole temp : exits) {
                id = temp.getId();
                map.put("id", id);
                int row = sysUserRoleDao.delete(params);
                if (row == 0) {
                    throw new BusinessException("删除用户角色关联信息错误");
                }
            }
        }
        for (SysUserRole sysUserRole : list) {
            sysUserRole.setCreator(LoginSysUserUtil.getUserId());
            int row = sysUserRoleDao.update(sysUserRole);
            if (row == 0) {
                throw new BusinessException("删除用户角色关联信息错误");
            }
        }
        result.setMsg("批量保存用户角色关联成功");
        result.setSuccess(true);
        return result;
    }

    /**
     * 根据用户主键获取
     *
     * @param userId
     * @return
     */
    public List<SysUserRole> queryByUserId(String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        return sysUserRoleDao.select(params);
    }

    /**
     * 分页查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<SysUserRole> queryPage(int pageNo, int pageSize) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize < 1) {
            pageSize = 20;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("firstResult", (pageNo - 1) * pageSize);
        params.put("offset", pageSize);
        return sysUserRoleDao.select(params);
    }
}
