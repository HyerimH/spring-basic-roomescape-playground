package roomescape.global.auth;

import static roomescape.global.exception.ErrorCode.EMPTY_TOKEN;
import static roomescape.global.exception.ErrorCode.FORBIDDEN;
import static roomescape.global.exception.ErrorCode.INVALID_TOKEN;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberService;
import roomescape.global.common.CookieUtil;
import roomescape.global.exception.CustomException;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = CookieUtil.extractTokenFromCookies(cookies);

        if (token == null) {
            throw new CustomException(EMPTY_TOKEN);
        }

        Long memberId = jwtTokenProvider.extractMemberId(token);
        Member member = memberService.getMemberById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN));

        if (isAdminRequest(request) && !member.isAdmin()) {
            throw new CustomException(FORBIDDEN);
        }

        request.setAttribute("loginMember", LoginMember.from(member));
        return true;
    }

    private boolean isAdminRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/admin");
    }
}
