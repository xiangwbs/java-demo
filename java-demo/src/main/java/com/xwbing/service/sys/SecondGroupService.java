package com.xwbing.service.sys;

import com.drore.cloud.sdk.builder.QueryBuilder;
import com.drore.cloud.sdk.builder.RecordBuilder;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.xwbing.entity.SysUser;
import com.xwbing.util.CommonEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明: 二组service
 * 项目名称: zdemo
 * 创建时间: 2017/6/12 15:25
 * 作者: xiangwb
 */
@Service
public class SecondGroupService extends BaseService {
    @Autowired
    private CloudQueryRunner run;
    @Autowired
    private QueryBuilder queryBuilder;
    @Autowired
    private RecordBuilder recordBuilder;

    public Pagination queryList(SysUser sysUser) {//单个表
        RequestExample example = new RequestExample(Integer.MAX_VALUE, 1);
        RequestExample.Criteria cri = example.create();
        RequestExample.Param param = example.createParam();
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());//匹配查询
        if (StringUtils.isNotEmpty(sysUser.getName())) {
            param.addTerm("name", sysUser.getName());
            cri.getMust().add(param);
        }
        if (StringUtils.isNotEmpty(sysUser.getCondition())) {//模糊查询
            param = example.createParam();
            param.addFuzzy("user_name", sysUser.getCondition());
            param.addFuzzy("tel", sysUser.getCondition());
            cri.getShould().add(param);
        }
        if (StringUtils.isNotEmpty(sysUser.getId())) {//排除查询
            //更新或新增时，id存在，排除自己
            param = example.createParam();
            param.addTerm("id", sysUser.getId());
            cri.getMustNot().add(param);
        }
        example.addSort("", "asc");//排序
        return queryBuilder.findListByRName(SysUser.table, example);
    }

    public Pagination queryPage(Pagination pagination, SysUser sysUser) {
        //第一种方法 多表关联
        StringBuffer sql = new StringBuffer("select * from " + SysUser.table + " where 1=1");
        sql.append(" and is_deleted='" + CommonEnum.YesOrNo.NO.getCode() + "'");
        if (StringUtils.isNotEmpty(sysUser.getName())) {
            sql.append(" and name like '%" + sysUser.getName() + "%'");
        }
        pagination = queryBuilder.execute(sql.toString(), pagination.getPage_size(), pagination.getCurrent_page());
        //第二种方法 单个表
        Map<String, Object> term = new HashMap<>();
        term.put("is_deleted", CommonEnum.YesOrNo.NO.getCode());
        if (StringUtils.isNotEmpty(sysUser.getName())) {
            term.put("name", sysUser.getName());
        }
        pagination = queryBuilder.findListByExample(SysUser.table, term, pagination.getCurrent_page(), pagination.getPage_size());
        return pagination;
    }

    public boolean checkName(String id, String name) {
        RequestExample example = new RequestExample(Integer.MAX_VALUE, 1);
        RequestExample.Criteria cri = example.create();
        if (StringUtils.isNotEmpty(id)) {
            RequestExample.Param param = example.createParam();
            param.addTerm("id", id);
            cri.getMustNot().add(param);
        }
        RequestExample.Param param = example.createParam();
        param.addTerm("name", name);
        cri.getMust().add(param);
        List<SysUser> list = super.queryList(SysUser.table, example, SysUser.class);
        return list == null || list.isEmpty();
    }
}
