package cn.worth.oauth2.handler;

import cn.worth.common.constant.CommonConstant;
import cn.worth.common.domain.R;
import cn.worth.oauth2.enums.AuthErrorEnum;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
 * 通过 验证失败之后转发/error，而/error未授权匿名用户可以访问，从而使授权错误进来次类
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = authException.getMessage();
        log.info("客户端未授权，禁止访问 {}, error:{}", request.getRequestURI(), message);

        response.setCharacterEncoding(CommonConstant.UTF8);
        response.setContentType(CommonConstant.CONTENT_TYPE);

        AuthErrorEnum invalidClient = AuthErrorEnum.INVALID_CLIENT;
        R<String> result = new R<>(new AccessDeniedException(invalidClient.getDesc()));
        int code = invalidClient.getCode();
        result.setCode(code);
        response.setStatus(code);
        String json = JSON.toJSONString(result);
        response.getWriter().print(json);
    }
}