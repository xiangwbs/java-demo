package com.xwbing.controller.httpClient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.xwbing.util.RestClientUtil;

/**
 * 说明: 调用外部http接口示例<br/>
 * 创建日期: 2017年3月7日 下午4:29:04 <br/>
 * 作者: xwb
 */
@Controller
public class HttpClientControl {

    /**
     * 
     * 功能描述：     调用http接口   json参数    post方式传参          <br/>
     * 作    者：xwb <br/>
     * 创建时间：2017年3月7日  下午4:36:01 <br/>
     * @param url
     * @param param
     * @return
     */
    @RequestMapping("/post")
    @ResponseBody
    public JSONObject doPost(String url,@RequestBody JSONObject param){
        return RestClientUtil.postByJson(url, param);
    }
}
