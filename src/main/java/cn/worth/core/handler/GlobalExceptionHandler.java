package cn.worth.core.handler;

import cn.worth.common.domain.R;
import cn.worth.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @Author: MyIreland on.
 * @Description: 全局的的异常拦截器
 * @Date: 2019/3/22
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常.
     *
     * @param e the e
     * @return R
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public R serviceException(BusinessException e) {
        String message = e.getMessage();
        log.info("出现异常信息:{}", message, e);
        return R.fail(message);
    }

//    @ExceptionHandler(OAuth2Exception.class)
//    @ResponseStatus(HttpStatus.OK)
//    public R oAuth2Exception(OAuth2Exception e) {
//        String message = e.getMessage();
//        log.info("登陆异常:{}", message, e);
//        return R.fail(message);
//    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R exception(Exception e) {
        String message = e.getMessage();
        log.info("出现异常信息:{}", message, e);
        return R.fail(message);
    }
}
