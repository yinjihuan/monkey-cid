package org.monkey.cid.core.exception;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monkey.cid.core.base.Response;
import org.monkey.cid.core.base.ResponseCode;
import org.monkey.cid.core.base.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author yinjihuan
 * @date 2018-09-25
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 系统异常处理
     *
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData<Object> defaultErrorHandler(HttpServletRequest req, Exception e, HttpServletResponse response) throws Exception {
    	// 参数验证处理
    	if (e instanceof MethodArgumentNotValidException) {
    		MethodArgumentNotValidException excetion = (MethodArgumentNotValidException) e;
    		BindingResult result = excetion.getBindingResult();
    		String message = result.getAllErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(","));
    		return Response.fail(message, ResponseCode.PARAM_ERROR_CODE);
    	} else {
    		response.setStatus(500);
            logger.error("内部异常：", e);
            return Response.fail(e.getMessage(), ResponseCode.SERVER_ERROR_CODE);
    	}
    }
    
    /**
     * 自定义异常处理
     *
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = GlobalException.class)
    @ResponseBody
    public ResponseData<Object> customerErrorHandler(HttpServletRequest req, GlobalException e, HttpServletResponse response) throws Exception {
        return Response.fail(e.getMessage(), e.getCode());
    }

}
