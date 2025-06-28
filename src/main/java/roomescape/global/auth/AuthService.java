package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.global.exception.CustomException;
import roomescape.global.exception.ErrorCode;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberDao;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public Cookie login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        String token = jwtTokenProvider.createToken(member);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public LoginMember checkLogin(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        if (token.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_TOKEN);
        }
        try {
            Long memberId = jwtTokenProvider.extractMemberId(token);
            Member member = memberDao.findById(memberId);
            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Cookie logout() {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
