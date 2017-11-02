package com.xwbing.controller.httpClient;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.util.RestClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 说明: 调用外部http接口示例
 * 创建日期: 2017年3月7日 下午4:29:04
 * 作者: xiangwb
 */
@Controller
@RequestMapping("/httpClient/")
public class HttpClientControl {

    /**
     * 功能描述：     调用http接口   json参数    post方式传参
     * 作    者：xwb <br/>
     * 创建时间：2017年3月7日  下午4:36:01
     *
     * @param url
     * @param param
     * @return
     */
    @PostMapping("/post")
    @ResponseBody//通过适当的HttpMessageConverter转换为指定格式后，写入到Response对象的body数据区
    public JSONObject doPost(String url, @RequestBody JSONObject param) {
        return RestClientUtil.postByJson(url, param);
    }
}
