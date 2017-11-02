package com.xwbing.advice;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.Exception.BusinessException;
import com.xwbing.util.JSONObjResult;
import com.xwbing.util.RestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * 说明: GlobalExceptionHandler
 * 创建日期: 2017年3月21日 下午2:53:35
 * 作者: xiangwb
 */
// 作用在所有注解了@RequestMapping的控制器的方法上
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 自定义业务异常
     * 
     * @param request
     * @param ex
     * @return
     */
    // 拦截处理控制器里对应的异常。
    @ExceptionHandler(value = BusinessException.class)
    // 返回给页面200状态码
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public JSONObject handlerGuideException(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage());
        RestMessage result = new RestMessage();
        result.setSuccess(false);
        result.setMsg(ex.getMessage());
        return JSONObjResult.toJSONObj(result);
    }

    /**
     * 
     * 表单检验(validator) 异常
     * 
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public JSONObject handlerBindException(HttpServletRequest request,
            HttpServletResponse response, BindException ex) {
        log.error(ex.getMessage());
        RestMessage result = new RestMessage();
        List<ObjectError> list = ex.getAllErrors();
        StringBuilder stringBuffer = new StringBuilder();
        for (ObjectError objectError : list) {
            if (stringBuffer.length() > 0)
                stringBuffer.append(" && ");
            stringBuffer.append(objectError.getDefaultMessage());
        }
        result.setSuccess(false);
        result.setMsg(stringBuffer.toString());
        response.setStatus(HttpStatus.OK.value());
        return JSONObjResult.toJSONObj(result);
    }

    /**
     * 服务器参数异常
     * 
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JSONObject handlerServerException(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage());
        RestMessage result = new RestMessage();
        result.setSuccess(false);
        result.setMsg(ex.getMessage());
        return JSONObjResult.toJSONObj(result);
    }

    /**
     * 
     * 功能描述： <br/>
     * 作 者：xwb <br/>
     * 创建时间：2017年4月5日 下午1:31:34 <br/>
     * 
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JSONObject handlerException(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage());
        return JSONObjResult.toJSONObj("系统异常，请联系管理员");
    }
}
