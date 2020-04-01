package cn.worth.oauth2.filter;

import cn.worth.common.constant.SecurityConstants;
import cn.worth.core.wrapper.MutableHttpServletRequestWrapper;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessFilter extends OncePerRequestFilter {

    private static final String ACCESS_START_TIME = "create_time";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MutableHttpServletRequestWrapper mutableRequest = new MutableHttpServletRequestWrapper(request);
        mutableRequest.putParameter(ACCESS_START_TIME, System.currentTimeMillis());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            mutableRequest.putHeader(SecurityConstants.USER_HEADER, authentication.getName());
            mutableRequest.putHeader(SecurityConstants.ROLE_HEADER,  CollectionUtil.join(authentication.getAuthorities(),","));
        }

        filterChain.doFilter(mutableRequest, response);
    }
}