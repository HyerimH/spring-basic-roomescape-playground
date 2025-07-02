package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.global.common.CookieUtil;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Cookie login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        String token = jwtTokenProvider.createToken(member);
        return CookieUtil.createTokenCookie(token);
    }

    public Cookie logout() {
        return CookieUtil.createLogoutCookie();
    }
}
