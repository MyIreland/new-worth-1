package cn.worth.oauth2.interceptor;

import cn.worth.common.constant.SecurityConstants;
import cn.worth.common.utils.StringUtils;
import cn.worth.oauth2.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 设置用户名称到线程中
         */
        String username = request.getHeader(SecurityConstants.USER_HEADER);
        if (StringUtils.isNotBlank(username)) {
            log.info("UserInterceptor get username:{}", username);
            UserUtils.setUser(username);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String username = request.getHeader(SecurityConstants.USER_HEADER);
        /*
         * 清除线程用户
         */
        if (StringUtils.isNotEmpty(username)) {
            UserUtils.clearAllUserInfo();
        }

    }
}