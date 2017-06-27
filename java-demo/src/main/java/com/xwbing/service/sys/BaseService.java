package com.xwbing.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.builder.QueryBuilder;
import com.drore.cloud.sdk.builder.RecordBuilder;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.xwbing.Exception.BusinessException;
import com.xwbing.util.PagerUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称: java-demo
 * 创建时间: 2017/6/19 17:11
 * 作者: xiangwb
 * 说明: 基础服务层
 */
public class BaseService {
    @Resource
    public QueryBuilder queryBuilder;
    @Resource
    public RecordBuilder recordBuilder;

    /**
     * 直接转换成对象
     *
     * @param id
     * @param tableName
     * @param classOfT
     * @return
     */
    public <T> T queryOne(String id, String tableName, Class<T> classOfT, String... displayFields) {
        Map<String, Object> data = queryBuilder.findOneByRName(tableName, id, displayFields);
        if (data.isEmpty() || data.size() == 0) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(data);
        return JSONObject.toJavaObject(jsonObject, classOfT);
    }

    /**
     * 条件查询第一条记录
     *
     * @param tableName
     * @param term
     * @param classOfT
     * @return
     */
    public <T> T queryFirstOne(String tableName, Map<String, Object> term, Class<T> classOfT) {
        Map<String, Object> data = queryBuilder.findFirstByRName(tableName, term);
        if (data.isEmpty() || data.size() == 0) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(data);
        return JSONObject.toJavaObject(jsonObject, classOfT);
    }

    /**
     * 分页查询
     *
     * @param tableName
     * @param term
     * @param pagerUtil
     * @param classOfT
     * @return
     */
    public <T> PagerUtil queryPage(String tableName, Map<String, Object> term, PagerUtil pagerUtil, Class<T> classOfT, String... displayFields) {
        Pagination<Map<String, Object>> page = queryBuilder.findListByExample(tableName, term, pagerUtil.getCurrentPage(), pagerUtil.getPageSize(), displayFields);
        if (page == null)
            return pagerUtil;
        List<Map<String, Object>> list = page.getData();
        if (list != null && !list.isEmpty()) {
            List<T> result = new ArrayList<>();
            list.forEach(map -> result.add(JSONObject.toJavaObject(new JSONObject(map), classOfT)));
            pagerUtil.setData(result);
        }
        pagerUtil.setCount(page.getCount());
        pagerUtil.setTotalPage(page.getTotal_page());
        pagerUtil.setSuccess(page.getSuccess());
        return pagerUtil;
    }

    /**
     * 列表条件查询
     *
     * @param <T>
     * @param tableName
     * @param example
     * @param classOfT
     * @param displayFields
     * @return
     * @throws IOException
     */
    public <T> List<T> queryList(String tableName, RequestExample example, Class<T> classOfT, String... displayFields) {
        if (null == example) {
            example = new RequestExample(Integer.MAX_VALUE, 1);
        }
        Pagination<Map<String, Object>> page = queryBuilder.findListByRName(tableName, example, displayFields);
        List<Map<String, Object>> list = page.getData();
        if (list != null && !list.isEmpty()) {
            List<T> result = new ArrayList<>();
            list.forEach(map -> result.add(JSONObject.toJavaObject(new JSONObject(map), classOfT)));
            return result;
        } else
            return null;
    }

    /**
     * SQL分页查询
     *
     * @param sql
     * @param pagerUtil
     * @param classOfT
     * @param <T>
     * @return
     */
    public <T> PagerUtil querySql(String sql, PagerUtil pagerUtil, Class<T> classOfT) {
        Pagination<Map<String, Object>> page = queryBuilder.execute(sql, pagerUtil.getPageSize(), pagerUtil.getCurrentPage());
        if (page == null) {
            return pagerUtil;
        }
        List<Map<String, Object>> list = page.getData();
        if (list != null && !list.isEmpty()) {
            List<T> result = new ArrayList<>();
            list.forEach(map -> result.add(JSONObject.toJavaObject(new JSONObject(map), classOfT)));
            pagerUtil.setData(result);
        }
        pagerUtil.setCount(page.getCount());
        pagerUtil.setTotalPage(page.getTotal_page());
        pagerUtil.setSuccess(page.getSuccess());
        return pagerUtil;
    }

    /**
     * 查询数量
     *
     * @param tableName
     * @param example
     * @param <T>
     * @return
     */
    public int queryCount(String tableName, RequestExample example) {
        if (null == example) {
            example = new RequestExample(Integer.MAX_VALUE, 1);
        }
        if (example.getSort() == null || example.getSort().isEmpty()) {
            HashMap<String, Object> sort = new HashMap<>();
            sort.put("sort", "ASC");
            example.setSort(sort);
        }
        Pagination<Map<String, Object>> page = queryBuilder.findListByRName(tableName, example);
        return page.getCount();
    }

    /**
     * 保存操作
     *
     * @param data
     * @param tableName
     * @return
     */
    public RestMessage saveObject(Object data, String tableName) {
        RestMessage restMessage = recordBuilder.createByRName(tableName, JSONObject.toJSON(data));
        if (restMessage == null) {
            restMessage = new RestMessage();
            restMessage.setSuccess(false);
        }
        if (!restMessage.isSuccess()) {
            throw new BusinessException("保存数据失败!");
        }
        return restMessage;
    }

    /**
     * 批量新增
     *
     * @param tableName
     * @param list
     * @return
     */
    public RestMessage saveBatch(String tableName, List<?> list) {
        RestMessage restMessage = recordBuilder.createBatchByRName(tableName, JSONObject.toJSON(list));
        if (restMessage == null) {
            restMessage = new RestMessage();
            restMessage.setSuccess(false);
        }
        if (!restMessage.isSuccess())
            throw new BusinessException("批量新增数据失败!");
        return restMessage;
    }

    /**
     * 单个删除
     *
     * @param id
     * @param tableName
     * @return
     */
    public RestMessage deleteById(String id, String tableName) {
        RestMessage restMessage = recordBuilder.deleteByRName(tableName, id);
        if (restMessage == null) {
            restMessage = new RestMessage();
            restMessage.setSuccess(false);
        }
        if (!restMessage.isSuccess())
            throw new BusinessException("删除数据失败!");
        return restMessage;
    }

    /**
     * 根据条件删除
     *
     * @param tableName
     * @param params
     * @return
     */
    public RestMessage deleteByCriterion(String tableName, Map<String, Object> params) {
        RestMessage restMessage = recordBuilder.deleteByCriterion(tableName, params);
        if (restMessage == null) {
            restMessage = new RestMessage();
            restMessage.setSuccess(false);
        }
        if (!restMessage.isSuccess())
            throw new BusinessException("删除数据失败!");
        return restMessage;
    }

    /**
     * 批量删除
     *
     * @param pkIds
     * @param tableName
     * @return
     * @throws IOException
     */
    public RestMessage deleteByIds(String[] pkIds, String tableName) {
        RestMessage restMessage = recordBuilder.deleteByRName(tableName, pkIds);
        if (restMessage == null) {
            restMessage = new RestMessage();
            restMessage.setSuccess(false);
        }
        if (!restMessage.isSuccess())
            throw new BusinessException("批量删除数据失败!");
        return restMessage;
    }

    /**
     * 修改数据
     *
     * @param id
     * @param data
     * @param tableName
     * @return
     */
    public RestMessage updateObject(String id, Object data, String tableName) {
        RestMessage restMessage = recordBuilder.updateByRName(tableName, id, JSONObject.toJSON(data));
        if (restMessage == null) {
            restMessage = new RestMessage();
            restMessage.setSuccess(false);
        }
        if (!restMessage.isSuccess()) {
            throw new BusinessException("修改数据失败!");
        }
        return restMessage;
    }

    /**
     * 批量更新
     *
     * @param tableName
     * @param list
     * @return
     */
    public RestMessage updateBatch(String tableName, List list) {
        RestMessage restMessage = recordBuilder.updateBatchByRName(tableName, JSONObject.toJSON(list));
        if (restMessage == null) {
            restMessage = new RestMessage();
            restMessage.setSuccess(false);
        }
        if (!restMessage.isSuccess()) {
            throw new BusinessException("批量更新数据失败!");
        }
        return restMessage;
    }
}
