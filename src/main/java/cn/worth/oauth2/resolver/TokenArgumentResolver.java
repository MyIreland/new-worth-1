package cn.worth.oauth2.resolver;

import cn.worth.common.annotation.CurrentUser;
import cn.worth.core.domain.LoginUser;
import cn.worth.oauth2.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Author: chenxiaoqing9
 * @Date: Created in 2019/3/19
 * Token转化LoginUser
 */
@Slf4j
@Configuration
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 1. 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // 如果参数类型是LoginUser并且有CurrentUser注解则支持
        return methodParameter.getParameterType().isAssignableFrom(LoginUser.class)
                && methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    /**
     * @param methodParameter       入参集合
     * @param modelAndViewContainer model 和 view
     * @param nativeWebRequest      web相关
     * @param webDataBinderFactory  入参解析
     * @return 包装对象
     * @throws Exception exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {
        LoginUser loginUser = UserUtils.getLoginUser();
        return loginUser;
    }
}
