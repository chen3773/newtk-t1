package com.tiktok.framework.web.exception;

import javax.servlet.http.HttpServletRequest;

import com.tiktok.common.core.redis.RedisCache;
import com.tiktok.common.utils.ParseTitle;
import com.tiktok.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.tiktok.common.constant.HttpStatus;
import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.core.text.Convert;
import com.tiktok.common.exception.DemoModeException;
import com.tiktok.common.exception.ServiceException;
import com.tiktok.common.utils.StringUtils;
import com.tiktok.common.utils.html.EscapeUtil;

/**
 * 全局异常处理器
 * 
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Autowired
    private RedisCache redisCache;

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    /**
     * 权限校验异常
     */
    @ExceptionHandler(CustomException.class)
    public AjaxResult CustomException(AccessDeniedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }





    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value))
        {
            value = EscapeUtil.clean(value);
        }
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), value));
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public AjaxResult handleBindException(IllegalArgumentException  e)
    {
        String message = e.getMessage();
        Long uid = null;

        try {
             uid = SecurityUtils.getLoginUser().getUser().getUid();
        }catch (Exception ee){
            uid = null;
        }
        String language = "English";
        if (uid != null) {
            // 从Redis中获取用户的语言设置
          String l =   (String) redisCache.getCacheObject("user:language:" + uid);;
            if(l!=null){
                language = l;
            }

        }
        String[] strings = ParseTitle.parseText(message);
        if (strings != null && strings.length >= 2 && strings[0] != null && strings[1] != null) {
                if(language.equals("Chinese")){
                    message =   strings[0];
                }else if(language.equals("English")){
                    message =  strings[1];
                }

        }
        return AjaxResult.error(message);
    }


//    /**
//     * 仿照业务异常——自定义异常抛出
//     */
//    @ExceptionHandler(ErrorCodeException.class)
//    public AjaxResult UserDefinedException(ErrorCodeException e){
//        System.out.println("StringUtils.isNull(e.getCode()):"+StringUtils.isNull(e.getCode()));
//        if (StringUtils.isNull(e.getCode()))
//        {
//            return AjaxResult.error(e.getMessage());
//        }
//        return AjaxResult.error(e.getCode(), e.getMessage());
//    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ErrorCodeException.class)
    public AjaxResult ErrorCodeException(ErrorCodeException  e)
    {
        String message = e.getMessage();
        Long uid = null;

        try {
            uid = SecurityUtils.getLoginUser().getUser().getUid();
        }catch (Exception ee){
            uid = null;
        }
        String language = "English";
        if (uid != null) {
            // 从Redis中获取用户的语言设置
            String l =   (String) redisCache.getCacheObject("user:language:" + uid);;
            if(l!=null){
                language = l;
            }

        }
        String[] strings = ParseTitle.parseText(message);
        if (strings != null && strings.length >= 2 && strings[0] != null && strings[1] != null) {
            if(language.equals("Chinese")){
                message =   strings[0];
            }else if(language.equals("English")){
                message =  strings[1];
            }

        }

        if (StringUtils.isNull(e.getCode()))
        {
            return AjaxResult.error(message);
        }
        return AjaxResult.error(e.getCode(),message);
    }



    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        return AjaxResult.error("演示模式，不允许操作");
    }
}
