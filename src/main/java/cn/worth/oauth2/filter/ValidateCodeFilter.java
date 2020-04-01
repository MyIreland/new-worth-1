package cn.worth.oauth2.filter;

import cn.worth.common.constant.CommonConstant;
import cn.worth.common.constant.SecurityConstants;
import cn.worth.common.domain.R;
import cn.worth.common.exception.BusinessException;
import cn.worth.oauth2.config.FilterIgnoreProperties;
import cn.worth.oauth2.utils.AuthUtils;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.json.JSONUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * <p>
 *  security.validate.code 默认 为false，开启需要设置为true
 * </p>
 *
 * @author zhangjinkun@xdf.cn
 * @date 2018/5/10
 *
 */
@Slf4j
//@Configuration("validateCodeFilter")
//@ConditionalOnProperty(value = "security.validate.code", havingValue = "true")
public class ValidateCodeFilter extends OncePerRequestFilter {
    private static final String WRONG_CAPTCHA_ERROR = "验证码错误，请重新输入";
    private static final String EXPIRED_CAPTCHA_ERROR = "验证码已过期，请重新获取";


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FilterIgnoreProperties filterIgnorePropertiesConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (shouldFilter(request)) {
            try {
                checkCode(request);
                filterChain.doFilter(request, response);
            } catch (BusinessException e) {
                response.setCharacterEncoding(CommonConstant.UTF8);
                response.setContentType(CommonConstant.CONTENT_TYPE);
                R<String> result = new R<>(new BusinessException(WRONG_CAPTCHA_ERROR));
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                String json = JSONUtil.toJsonStr(result);
                response.getWriter().print(json);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 是否校验验证码
     * 1. 判断验证码开关是否开启
     * 2. 判断请求是否登录请求
     * 3. 判断终端是否支持
     *
     * @return true/false
     */
    public boolean shouldFilter(HttpServletRequest request) {
        /**
         * 获取token才需要
         */
        if (!StrUtil.containsAnyIgnoreCase(request.getRequestURI(),
                SecurityConstants.OAUTH_TOKEN_URL)) {
            return false;
        }

        /**
         * 检查请求头的Base64(clientid:clientsecret)
         */
        try {
            String[] clientInfos = AuthUtils.extractAndDecodeHeader(request);
            if (CollUtil.containsAny(filterIgnorePropertiesConfig.getClients(), Arrays.asList(clientInfos))) {
                return false;
            }
        } catch (IOException e) {
            log.error("解析终端信息失败", e);
        }

        return true;
    }

    /**
     * 检查code
     *
     * @param httpServletRequest request
     * @throws BusinessException 验证码校验异常
     */
    private void checkCode(HttpServletRequest httpServletRequest) throws BusinessException {
        String code = httpServletRequest.getParameter("code");
        if (StrUtil.isBlank(code)) {
            throw new BusinessException("请输入验证码");
        }

        String randomStr = httpServletRequest.getParameter("randomStr");
        if (StrUtil.isBlank(randomStr)) {
            randomStr = httpServletRequest.getParameter("mobile");
        }

        String key = SecurityConstants.DEFAULT_CODE_KEY + randomStr;
        if (!redisTemplate.hasKey(key)) {
            throw new BusinessException(EXPIRED_CAPTCHA_ERROR);
        }

        Object codeObj = redisTemplate.opsForValue().get(key);

        if (codeObj == null) {
            throw new BusinessException(EXPIRED_CAPTCHA_ERROR);
        }

        String saveCode = codeObj.toString();
        if (StrUtil.isBlank(saveCode)) {
            redisTemplate.delete(key);
            throw new BusinessException(EXPIRED_CAPTCHA_ERROR);
        }

        if (!StrUtil.equals(saveCode, code)) {
            redisTemplate.delete(key);
            throw new BusinessException(WRONG_CAPTCHA_ERROR);
        }

        redisTemplate.delete(key);
    }
}
