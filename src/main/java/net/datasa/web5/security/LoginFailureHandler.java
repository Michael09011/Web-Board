package net.datasa.web5.security;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        log.debug("로그인 실패");
        String errorMessage = "";

        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디나 비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "비활성화된 계정입니다. 고객센터에 문의하세요.";
        } else {
            errorMessage = "로그인에 실패했습니다. 관리자에게 문의하세요.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/member/login?error=true&message=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}
