package cn.worth.oauth2.utils;

import cn.worth.common.constant.CommonConstant;
import cn.worth.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class AuthUtils {
    private static final String BASIC_ = "Basic ";

    public static String getBearerToken(HttpServletRequest request) {
        return request.getHeader(CommonConstant.AUTH_HEADER).split(CommonConstant.TOKEN_SPLIT)[1];
    }

    /**
     * 从header 请求中的clientId/clientsecect
     *
     * @param header header中的参数
     * @throws BusinessException if the Basic header is not present or is not valid
     *                           Base64
     */
    public static String[] extractAndDecodeHeader(String header)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, CommonConstant.UTF8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BusinessException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

    /**
     * *从header 请求中的clientId/clientsecect
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String[] extractAndDecodeHeader(HttpServletRequest request)
            throws IOException {
        String header = request.getHeader(CommonConstant.AUTH_HEADER);

        if (header == null || !header.startsWith(BASIC_)) {
            throw new BusinessException("请求头中client信息为空");
        }

        return extractAndDecodeHeader(header);
    }

    public static void main(String[] args) {
        String s = Base64.getEncoder().encodeToString(new String("acme:secret").getBytes());
        System.out.println(s);
    }
}
