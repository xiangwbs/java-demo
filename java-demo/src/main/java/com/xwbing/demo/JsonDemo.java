package com.xwbing.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xwbing.entity.BaseEntity;
import com.xwbing.entity.SysUser;
import com.xwbing.util.RestMessage;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称: zxwbing
 * 创建日期: 2017年3月18日 下午1:34:05
 * 作者: xiangwb
 */
@Data
public class JsonDemo {
    /*
     * fastJson注解
     */
    @JSONField(serialize = false)
    private String name;
    @JSONField(name = "nick_name")
    private String nickName;
    /*
     * gson注解
     */
    @SerializedName("user_age")
    //可以序列化和反序列化
    @Expose
    private String userAge;
    //临时字段，可以反序列化
    @Expose(serialize = false)
    private String addr;

    public static void main(String[] args) {
        /**
         * fastJson
         */
        String jsonStr = "{'id':'888888','resultMessage':{'msg':'200','isSuccess':'true'}}";
        Map<String, Object> map = new HashMap<>();
        map.put("id", "99999");
        List<SysUser> list = new ArrayList<>();
        /*
         * to jsonObject
         */
        JSONObject jsonObject = new JSONObject(map);// map转JSONObject
        jsonObject = JSONObject.parseObject(jsonStr);// json字符串转JSONObject
        /*
         * to javaObject
         */
        BaseEntity entiry = JSONObject.toJavaObject(jsonObject,
                BaseEntity.class);//jsonObject转javaObject
        entiry = JSONObject.parseObject(jsonStr, BaseEntity.class);// json字符串转javaObject
        /*
         * JSONArray <--> list
         */
        String arrayStr = JSONArray.toJSONString(list);
        list = JSONArray.parseArray(arrayStr, SysUser.class);
        /*
         * to object
         */
        Object obj = JSONObject.toJSON(entiry);// javabean转object
        /*
         * to jsonStr
         */
        jsonStr = JSONObject.toJSONString(entiry);
        /*
         * 获取jsonObject属性
         */
        String id = jsonObject.getString("id");
        RestMessage result = JSONObject.toJavaObject(
                jsonObject.getJSONObject("resultMessage"), RestMessage.class);

        /**
         * gson
         */
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        JsonDemo jsonDemo = new JsonDemo();
        jsonDemo.setUserAge("18");
        jsonDemo.setAddr("西溪湿地");
        String serialize = gson.toJson(jsonDemo);
        String s = new Gson().toJson(jsonDemo);
        JsonDemo deserialize = gson.fromJson(s, JsonDemo.class);


        String gsonStr = "{'name':'John', 'sex':1,'role':{'id':'33'}}";
        /*
         * jsonStr to JsonElement
         */
        JsonElement jsonElement = new JsonParser().parse(gsonStr);
        /*
         * jsonStr to javaObject
         */
        SysUser user = new Gson().fromJson(gsonStr, SysUser.class);
        /*
         * JsonElement to javaObject
         */
        user = new Gson().fromJson(jsonElement, SysUser.class);
        /*
         * javaObject to jsonStr
         */
        gsonStr = new Gson().toJson(user);
        /*
         * 获取属性
         */
        JsonObject gsonObject = jsonElement.getAsJsonObject();
        JsonElement role = jsonElement.getAsJsonObject().get("role");
        String name = jsonElement.getAsJsonObject().get("name").getAsString();
    }
}
