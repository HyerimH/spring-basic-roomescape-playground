package roomescape.auth;

import static roomescape.exception.ErrorCode.EMPTY_TOKEN;
import static roomescape.exception.ErrorCode.INVALID_TOKEN;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberService;
import roomescape.exception.CustomException;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = cookieUtils.extractTokenFromCookies(cookies);

        if (StringUtils.isBlank(token)) {
            throw new CustomException(EMPTY_TOKEN);
        }

        Long memberId = jwtUtils.extractMemberId(token);
        Member member = memberService.getMemberById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN));

        request.setAttribute("loginMember", LoginMember.from(member));
        return true;
    }
}
