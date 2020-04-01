package cn.worth.oauth2.handler;

import cn.worth.oauth2.enums.AuthErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component("customWebResponseExceptionTranslator")
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Autowired
    private MessageSource messageSource;

    private static Locale locale = LocaleContextHolder.getLocale();

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error(e.getMessage());
            if (e instanceof InvalidTokenException) {
            InvalidTokenException invalidTokenException = (InvalidTokenException) e;
            if("Token was not recognised".equals(invalidTokenException.getMessage())){
                return ResponseEntity
                        .status(invalidTokenException.getHttpErrorCode())
                        .body(new OAuth2Exception(messageSource.getMessage("tokenWasNotRecognised", null, locale)));
            }
        }

        if (e instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            AuthErrorEnum errorAccountOrPassword = AuthErrorEnum.ERROR_ACCOUNT_OR_PASSWORD;
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new OAuth2Exception(errorAccountOrPassword.getDesc()));
        } else if (e instanceof DisabledException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(new OAuth2Exception(e.getMessage()));
        } else {
            return ResponseEntity
                    .status(500)
                    .body(new OAuth2Exception(e.getMessage()));
        }
    }
}