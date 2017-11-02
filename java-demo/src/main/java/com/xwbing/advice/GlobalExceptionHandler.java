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
 * 说明: GlobalExceptionHandler
 * 创建日期: 2017年3月21日 下午2:53:35
 * 作者: xiangwb
 */
// 作用在所有注解了@RequestMapping的控制器的方法上
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 自定义业务异常
     *
     * @param ex
     * @return
     */
    // 拦截处理控制器里对应的异常。
    @ExceptionHandler(value = BusinessException.class)
    // 返回给页面200状态码
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public JSONObject handlerGuideException(Exception ex) {
        logger.error(ex.getMessage());
        return JSONObjResult.toJSONObj(ex.getMessage());
    }

    /**
     * 服务器参数异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JSONObject handlerServerException(Exception ex) {
        logger.error(ex.getMessage());
        return JSONObjResult.toJSONObj(ex.getMessage());
    }

    /**
     * 表单检验(validator) 异常
     *
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public JSONObject handlerBindException(HttpServletRequest request, HttpServletResponse response, BindException ex) {
        logger.error(ex.getMessage());
        RestMessage result = new RestMessage();
        List<ObjectError> list = ex.getAllErrors();
        StringBuilder stringBuffer = new StringBuilder();
        for (ObjectError objectError : list) {
            if (stringBuffer.length() > 0)
                stringBuffer.append(" && ");
            stringBuffer.append(objectError.getDefaultMessage());
        }
        result.setMsg(stringBuffer.toString());
        result.setSuccess(false);
        response.setStatus(HttpStatus.OK.value());
        return JSONObjResult.toJSONObj(result);
    }

    /**
     * 全部捕捉
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JSONObject handlerException(Exception ex) {
        logger.error(ex.getMessage());
        return JSONObjResult.toJSONObj("系统异常，请联系管理员");
    }
}
