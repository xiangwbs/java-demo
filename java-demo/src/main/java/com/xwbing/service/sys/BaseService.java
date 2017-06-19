package com.xwbing.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.builder.QueryBuilder;
import com.drore.cloud.sdk.builder.RecordBuilder;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.xwbing.Exception.BusinessException;
import com.xwbing.util.PageUtil;
import org.apache.commons.collections.MapUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: xiangwb
 * Description: 基础服务层
 */
public class BaseService {
    @Resource
    private QueryBuilder queryBuilder;
    @Resource
    private RecordBuilder recordBuilder;

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
     * @param pageUtil
     * @param classOfT
     * @return
     */
    public <T> PageUtil queryPage(String tableName, Map<String, Object> term, PageUtil pageUtil, Class<T> classOfT, String... displayFields) {
        Pagination<Map<String, Object>> page = queryBuilder.findListByExample(tableName, term, pageUtil.getCurrentPage(), pageUtil.getPageSize(), displayFields);
        if (page == null)
            return pageUtil;
        List<Map<String, Object>> list = page.getData();
        if (list != null && !list.isEmpty()) {
            List<T> result = new ArrayList<>();
            JSONObject jsonObject;
            for (Map<String, Object> map : list) {
                jsonObject = new JSONObject(map);
                result.add(JSONObject.toJavaObject(jsonObject, classOfT));
            }
            pageUtil.setRoot(result);
        }
        pageUtil.setCount(page.getCount());
        pageUtil.setTotalPage(page.getTotal_page());
        pageUtil.setSuccess(page.getSuccess());
        return pageUtil;
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
            JSONObject jsonObject;
            List<T> result = new ArrayList<>();
            for (Map<String, Object> map : list) {
                jsonObject = new JSONObject(map);
                result.add(JSONObject.toJavaObject(jsonObject, classOfT));
            }
            return result;
        } else
            return null;
    }

    /**
     * SQL分页查询
     *
     * @param sql
     * @param pageUtil
     * @param classOfT
     * @param <T>
     * @return
     */
    public <T> PageUtil querySql(String sql, PageUtil pageUtil, Class<T> classOfT) {
        Pagination<Map<String, Object>> page = queryBuilder.execute(sql, pageUtil.getPageSize(), pageUtil.getCurrentPage());
        if (page == null) {
            return pageUtil;
        }
        List<Map<String, Object>> list = page.getData();
        if (list != null && !list.isEmpty()) {
            List<T> result = new ArrayList<>();
            JSONObject jsonObject;
            for (Map<String, Object> map : list) {
                jsonObject = new JSONObject(map);
                result.add(JSONObject.toJavaObject(jsonObject, classOfT));
            }
            pageUtil.setRoot(result);
        }
        pageUtil.setCount(page.getCount());
        pageUtil.setTotalPage(page.getTotal_page());
        pageUtil.setSuccess(page.getSuccess());
        return pageUtil;
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
        if (MapUtils.isEmpty(example.getSort())) {
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
