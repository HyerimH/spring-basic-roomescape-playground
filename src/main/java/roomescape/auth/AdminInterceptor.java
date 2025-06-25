package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = jwtTokenProvider.extractTokenFromCookie(request.getCookies());
        LoginMember member = authService.checkLogin(token);

        if (member == null || !member.isAdmin()) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
