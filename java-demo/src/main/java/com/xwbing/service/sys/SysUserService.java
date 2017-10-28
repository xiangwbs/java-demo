package com.xwbing.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xwbing.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwbing.Exception.BusinessException;
import com.xwbing.dao.SysUserDao;
import com.xwbing.entity.SysAuthority;
import com.xwbing.entity.SysRole;
import com.xwbing.entity.SysUser;
import com.xwbing.entity.SysUserRole;
import com.xwbing.entity.dto.userDto;
import com.xwbing.entity.model.EmailModel;
import com.xwbing.util.CommonEnum.YesOrNoEnum;

/**
 * 说明:
 * 项目名称: zdemo
 * 创建日期: 2017年1月18日 下午5:46:16
 * 作者: xiangwb
 */
@Service
public class SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysAuthorityService sysAuthorityService;
	/**
	 * 
	 * 功能描述： 保存 此处要处理密码加盐，初始密码，发送邮件 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月20日 下午5:00:05 <br/>
	 * 
	 * @param user
	 * @return
	 */
	public RestMessage save(SysUser user) {
		RestMessage result = new RestMessage();
		SysUser tem = queryUserName(user.getUserName());
		if (tem != null) {
			throw new BusinessException("已经存在此用户名");
		}
		user.setId(PassWordUtil.createId());
		// 获取初始密码
		String[] res = PassWordUtil.getUserSecret(null, null);

		user.setSalt(res[1]);
		user.setPassword(res[2]);
		// 设置否管理员
		user.setIsAdmin(CommonEnum.YesOrNoEnum.NO.getCode());
		user.setCreator(LoginSysUserUtil.getUserId());
		int row = sysUserDao.save(user);
		if (row == 0) {
			throw new BusinessException("新增用户失败");
		}
		// 发送邮件
		EmailModel emailModel = new EmailModel();
		emailModel.setServerHost("smtp.163.com");
		emailModel.setProtocol("smtp");
		emailModel.setAuth(true);
		emailModel.setFromEmail("xwbing2009@163.com");
		emailModel.setToEmail(user.getMail());
		emailModel.setAttachFileNames(null);
		emailModel.setPassword("xwbing900417");
		emailModel.setSubject("用户认证中心邮箱服务器校验");
		emailModel.setCentent("此邮件是用户认证中心邮箱服务器校验，由系统发出，请勿回复 。你的用户名是："
				+ user.getUserName() + "密码是：" + res[0]);
		// 发送邮件结束
		boolean sucess = EmailUtil.sendTextEmail(emailModel);
		if (!sucess) {
			throw new BusinessException("发送密码邮件错误");
		}
		result.setMsg("新增用户成功");
		result.setSuccess(true);
		return result;
	}
	/**
	 * 
	 * 功能描述：        删除用户                <br/>
	 * 作    者：xwb <br/>
	 * 创建时间：2017年1月20日  下午5:17:42 <br/>
	 * @param id
	 * @param loginUserId
	 * @return
	 */
	public RestMessage deleteUsr(String id, String loginUserId) {
		RestMessage result = new RestMessage();
		SysUser old = findById(id);
		if (old == null) {
			throw new BusinessException("没有查询到对象");
		}
		if (loginUserId.equalsIgnoreCase(id)) {
			throw new BusinessException("不能删除当前登录用户");
		}
		if (CommonEnum.YesOrNoEnum.YES.getCode().equalsIgnoreCase(old.getIsAdmin())) {
			throw new BusinessException("不能对管理员进行删除操作");
		}
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id", id);
		int row = sysUserDao.delete(params);
		if(row==0){
			throw new BusinessException("删除用户失败");
		}
		List<SysUserRole> uRoles =sysUserRoleService.queryByUserId(id);
		if (uRoles != null && uRoles.size() > 0) {
			for (SysUserRole uRole:uRoles) {
				String urid=uRole.getId();
				sysUserRoleService.deleteById(urid);
			}
		}
		result.setMsg("删除用户成功");
		result.setSuccess(true);
		return result;
	}
	
	
	/**
	 * 功能描述：修改用户最后登录信息 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月20日 下午4:09:52 <br/>
	 * 
	 * @param id
	 * @param ip
	 * @param date
	 * @return
	 */
	public RestMessage updateLoginInfo(String id, String ip, Date date) {
		SysUser user = sysUserDao.findById(id);
		if (user == null) {
			throw new BusinessException("未查找到用户信息");
		}
		user.setLastLoginIp(ip);
		user.setLastLoginTime(date);
		user.setModifier(LoginSysUserUtil.getUserId());
		RestMessage result = new RestMessage();
		int row = sysUserDao.update(user);
		if (row == 1) {
			result.setMsg("修改用户最后登录信息成功");
			result.setSuccess(true);
		} else {
			result.setMsg("修改用户最后登录信息失败");
		}
		return result;
	}

	/**
	 * 功能描述： 修改密码 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月20日 下午4:14:24 <br/>
	 * 
	 * @param newPassWord
	 * @param oldPassWord
	 * @param loginUserId
	 * @return
	 */
	public RestMessage updatePassWord(String newPassWord, String oldPassWord,
                                      String loginUserId) {
		SysUser user = sysUserDao.findById(loginUserId);
		// 根据密码盐值， 解码
		byte[] salt = EncodeUtils.hexDecode(user.getSalt());
		byte[] hashPassword = Digests.sha1(oldPassWord.getBytes(), salt,
				SysUser.HASH_INTERATIONS);
		// 密码 数据库中密码
		String validatePassWord = EncodeUtils.hexEncode(hashPassword);

		if (!user.getPassword().equals(validatePassWord)) {// 如果不相等
			throw new BusinessException("原密码错误，请重新输入");
		}
		String[] str = PassWordUtil.getUserSecret(newPassWord, null);
		user.setSalt(str[1]);
		user.setPassword(str[2]);
		user.setModifier(LoginSysUserUtil.getUserId());
		int row = sysUserDao.update(user);
		RestMessage result = new RestMessage();
		if (row == 1) {
			result.setMsg("修改密码成功");
			result.setSuccess(true);
		} else {
			result.setMsg("修改密码失败");
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 
	 * 功能描述： 根据用户名查找 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月20日 下午4:29:10 <br/>
	 * 
	 * @param userName
	 * @return
	 */
	public SysUser queryUserName(String userName) {
		SysUser user = null;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("user_name", userName);
		List<SysUser> list = sysUserDao.select(data);
		if (list.size() > 0 && list != null) {
			user = list.get(0);
		}
		return user;
	}

	/**
	 * 
	 * 功能描述： 根据id查询 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月20日 下午4:29:23 <br/>
	 * 
	 * @param id
	 * @return
	 */
	public SysUser findById(String id) {
		SysUser user = sysUserDao.findById(id);
		return user;
	}

	

	/**
	 * 
	 * 功能描述： 更新 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月20日 下午5:04:05 <br/>
	 * 
	 * @param user
	 * @param loginUserId
	 * @return
	 */
	public RestMessage update(SysUser user, String loginUserId) {
		RestMessage result = new RestMessage();
		SysUser old = findById(user.getId());
		if (old == null) {
			throw new BusinessException("没有查询到对象");
		}

		if (loginUserId.equalsIgnoreCase(user.getId())) {
			throw new BusinessException("不能修改当前登录用户");
		}

		if (YesOrNoEnum.YES.getCode().equalsIgnoreCase(old.getIsAdmin())) {
			throw new BusinessException("不能对管理员修改操作");
		}
		// 根据实际情况补充
		old.setName(user.getName());
		old.setMail(user.getMail());
		old.setSex(user.getSex());
		old.setUserName(user.getUserName());
		old.setModifier(LoginSysUserUtil.getUserId());
		int row = sysUserDao.update(user);
		if (row == 1) {
			result.setMsg("更新用户成功");
			result.setSuccess(true);
		} else {
			result.setMsg("更新用户失败");
			result.setSuccess(false);
		}
		return result;
	}

	
	/**
	 * 
	 * 功能描述：     根据用户主键，是否有效，查找所拥有的权限                   <br/>
	 * 作    者：xwb <br/>
	 * 创建时间：2017年1月20日  下午5:18:16 <br/>
	 * @param userId
	 * @param enable
	 * @return
	 */
	public List<SysAuthority> queryAuthority(String userId, String isEnable) {
		List<SysAuthority> list=new ArrayList<SysAuthority>();
		List<SysRole> listRole=sysRoleService.queryByUserId(userId, isEnable);
		if (listRole == null || listRole.size() == 0)
			return list;
		List<SysAuthority> temp = null;
		for (SysRole role : listRole) {
			temp = sysAuthorityService.queryByRoleId(role.getId(), isEnable);
			if (temp != null && temp.size() > 0) {
				for (SysAuthority auth : temp) {
					if (list.contains(auth))
						continue;// 如果存在，那么去除
					list.add(auth);
				}
			}
		}
		return list;
	}
	/**
	 * 
	 * 功能描述：       模拟查询                 <br/>
	 * 作    者：xwb <br/>
	 * 创建时间：2017年2月22日  下午5:36:26 <br/>
	 * @return
	 */
	public List<SysUser> findList(){
	    return null;
	}
	/**
	 * 
	 * 功能描述：  获取excel导出列表所需数据                      <br/>
	 * 作    者：xwb <br/>
	 * 创建时间：2017年3月22日  下午5:06:08 <br/>
	 * @return
	 */
	public List<userDto> getReportList(){
	    List<userDto> listDto = new ArrayList<userDto>();
        List<SysUser> list =findList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (SysUser info : list) {
                userDto temp = new userDto();
                temp.setIsAdmin(CommonEnum.YesOrNoEnum.YES.getCode().equals(info.getIsAdmin())?"是":"否");
                temp.setMail(info.getMail());
                temp.setSex(1==info.getSex()?"男":"女");
                temp.setUserName(info.getUserName());
                listDto.add(temp);
            }
        }
        return listDto;
	}
}
