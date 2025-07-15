package roomescape.auth;

import static roomescape.exception.ErrorCode.EMPTY_TOKEN;
import static roomescape.exception.ErrorCode.FORBIDDEN;
import static roomescape.exception.ErrorCode.INVALID_TOKEN;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.CustomException;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final CookieUtils cookieUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String token = cookieUtils.extractTokenFromCookies(cookies);

        if (StringUtils.isBlank(token)) {
            throw new CustomException(EMPTY_TOKEN);
        }

        LoginMember loginMember = (LoginMember) request.getAttribute("loginMember");

        if (loginMember == null) {
            throw new CustomException(INVALID_TOKEN);
        }

        if (!loginMember.isAdmin()) {
            throw new CustomException(FORBIDDEN);
        }

        return true;
    }
}
