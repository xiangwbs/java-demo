package com.xwbing.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 作者: xiangwb
 * 说明: 基础服务层
 */
public class BaseService {
    @Resource
    public CloudQueryRunner runner;

    /**
     * 直接转换成对象
     *
     * @param id
     * @param tableName
     * @param classOfT
     * @return
     */
    public <T> T queryOne(String id, String tableName, Class<T> classOfT, String... displayFields) {
        Map<String, Object> data = runner.queryOne(tableName, id, displayFields);
        if (data.isEmpty())
            return null;
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
        Map data = runner.queryFirstByRName(tableName, term);
        if (data.isEmpty())
            return null;
        JSONObject jsonObject = new JSONObject(data);
        return JSONObject.toJavaObject(jsonObject, classOfT);
    }

    /**
     * 分页查询
     *
     * @param tableName
     * @param term
     * @param pagination
     * @param classOfT
     * @return
     */
    public <T> Pagination queryPage(String tableName, Map<String, Object> term, Pagination pagination, Class<T> classOfT, String... displayFields) {
        pagination = runner.queryListByExample(tableName, term, pagination.getCurrent_page(), pagination.getPage_size(), displayFields);
        List<Map<String, Object>> list = pagination.getData();
        if (list != null && list.size() > 0) {
            List<T> result = new ArrayList<>();
            list.forEach(map -> result.add(JSONObject.toJavaObject(new JSONObject(map), classOfT)));
            pagination.setData(result);
        }
        return pagination;
    }

    /**
     * SQL分页查询
     *
     * @param sql
     * @param pagination
     * @param classOfT
     * @param <T>
     * @return
     */
    public <T> Pagination querySql(String sql, Pagination pagination, Class<T> classOfT) {
        pagination = runner.sql(sql, pagination.getCurrent_page(), pagination.getPage_size());
        List<Map<String, Object>> list = pagination.getData();
        if (list != null && list.size() > 0) {
            List<T> result = new ArrayList<>();
            list.forEach(map -> result.add(JSONObject.toJavaObject(new JSONObject(map), classOfT)));
            pagination.setData(result);
        }
        return pagination;
    }

    /**
     * 列表条件查询
     *
     * @param tableName
     * @param term
     * @param classOfT
     * @param displayFields
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(String tableName, Map<String, Object> term, Class<T> classOfT, String... displayFields) {
        Pagination<Map> page = runner.queryListByExample(tableName, term, 1, Integer.MAX_VALUE, displayFields);
        List<Map> list = page.getData();
        if (list != null && list.size() > 0) {
            List<T> result = new ArrayList<>();
            list.forEach(map -> result.add(JSONObject.toJavaObject(new JSONObject(map), classOfT)));
            return result;
        } else
            return Collections.emptyList();
    }

    /**
     * 列表条件查询
     *
     * @param tableName
     * @param example
     * @param classOfT
     * @param displayFields
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(String tableName, RequestExample example, Class<T> classOfT, String... displayFields) {
        Pagination<Map> page = runner.queryListByExample(tableName, example, displayFields);
        List<Map> list = page.getData();
        if (list != null && list.size() > 0) {
            List<T> result = new ArrayList<>();
            list.forEach(map -> result.add(JSONObject.toJavaObject(new JSONObject(map), classOfT)));
            return result;
        } else
            return Collections.emptyList();
    }

    /**
     * 保存操作
     *
     * @param data
     * @param tableName
     * @return
     */
    public RestMessage saveObject(Object data, String tableName) {
        RestMessage restMessage = runner.insert(tableName, JSONObject.toJSON(data));
        if (!restMessage.isSuccess())
            restMessage.setMessage("保存数据失败!");
        return restMessage;
    }

    /**
     * 批量新增
     *
     * @param tableName
     * @param list
     * @return
     */
    public RestMessage saveBatch(String tableName, List list) {
        RestMessage restMessage = runner.insertBatch(tableName, JSONObject.toJSON(list));
        if (!restMessage.isSuccess())
            restMessage.setMessage("批量新增数据失败!");
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
        RestMessage restMessage = runner.delete(tableName, id);
        if (!restMessage.isSuccess())
            restMessage.setMessage("删除数据失败!");
        return restMessage;
    }

    /**
     * 根据条件删除
     *
     * @param tableName
     * @param params
     * @return
     */
    public RestMessage deleteByParam(String tableName, Map<String, Object> params) {
        RestMessage restMessage = runner.deleteByCriterion(tableName, new JSONObject(params));
        if (!restMessage.isSuccess())
            restMessage.setMessage("删除数据失败!");
        return restMessage;
    }

    /**
     * 批量删除
     *
     * @param pkIds
     * @param tableName
     * @return
     */
    public RestMessage deleteByIds(String[] pkIds, String tableName) {
        RestMessage restMessage = runner.delete(tableName, pkIds);
        if (!restMessage.isSuccess())
            restMessage.setMessage("批量删除数据失败!");
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
        RestMessage restMessage = runner.update(tableName, id, JSONObject.toJSON(data));
        if (!restMessage.isSuccess()) {
            restMessage.setMessage("修改数据失败!");
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
        RestMessage restMessage = runner.updateBatch(tableName, JSONObject.toJSON(list));
        if (!restMessage.isSuccess()) {
            restMessage.setMessage("批量更新数据失败!");
        }
        return restMessage;
    }
}
