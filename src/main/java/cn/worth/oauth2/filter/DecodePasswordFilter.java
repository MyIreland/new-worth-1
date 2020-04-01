package cn.worth.oauth2.filter;

import cn.worth.common.constant.SecurityConstants;
import cn.worth.core.wrapper.MutableHttpServletRequestWrapper;
import com.xiaoleilu.hutool.codec.Base64;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 前端密码解密过滤器
 * @author chenxiaoqing
 * @date 2018/8/24
 */
@Slf4j
//@Configuration
//@ConditionalOnProperty(value = "security.encode.key")
public class DecodePasswordFilter extends OncePerRequestFilter {

    private static final String PASSWORD = "password";
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/NOPadding";
    @Value("${security.encode.key}")
    private String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MutableHttpServletRequestWrapper mutableRequest = new MutableHttpServletRequestWrapper(request);
        if (shouldFilter(request)) {
            String password = request.getParameter(PASSWORD);
            if (StrUtil.isNotBlank(password)) {
                try {
                    password = decryptAES(password, key);
                } catch (Exception e) {
                    log.error("密码解密失败:{}", password);
                }
                mutableRequest.putParameter(PASSWORD, password.trim());
            }
        }

        filterChain.doFilter(mutableRequest, response);
    }

    public boolean shouldFilter(HttpServletRequest request) {
        if (!StrUtil.containsAnyIgnoreCase(request.getRequestURI(),
                SecurityConstants.OAUTH_TOKEN_URL)) {
            return false;
        }

        return true;
    }

    private static String decryptAES(String data, String pass) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        SecretKeySpec keyspec = new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM);
        IvParameterSpec ivspec = new IvParameterSpec(pass.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] result = cipher.doFinal(Base64.decode(data.getBytes(CharsetUtil.UTF_8)));
        return new String(result, CharsetUtil.UTF_8);
    }
}
