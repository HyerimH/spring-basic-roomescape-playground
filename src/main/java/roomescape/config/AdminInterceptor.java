package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.LoginMember;
import roomescape.member.MemberService;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = memberService.extractTokenFromCookie(request.getCookies());
        LoginMember member = memberService.checkLogin(token);

        if (member == null || !member.isAdmin()) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
