package com.xwbing.entity.vo;

import com.xwbing.entity.SysAuthority;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2016<br/>
 * 说明:权限树vo <br/>
 * 项目名称: guided-assistant-manage <br/>
 * 创建日期: 2016年8月16日 下午7:53:29 <br/>
 * 作者: lj
 */
@Data
public class AuthVo extends SysAuthority {
    /**
     * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
     * 说明: <br/>
     * 项目名称: drore-tenant-manage <br/>
     * 创建日期: 2016年9月27日 下午1:30:07 <br/>
     * 作者: wdz
     */
    private static final long serialVersionUID = 524028812401017834L;

    public AuthVo(SysAuthority orig) {
        try {
            BeanUtils.copyProperties(this, orig);//org.apache.commons.beanutils.BeanUtils
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<AuthVo> children;
}
