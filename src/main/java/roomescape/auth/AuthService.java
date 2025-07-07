package roomescape.auth;

import static roomescape.exception.ErrorCode.INVALID_EMAILPASSWORD;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.exception.CustomException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Cookie login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        if (member == null) {
            throw new CustomException(INVALID_EMAILPASSWORD);
        }
        String token = jwtTokenProvider.createToken(member);
        return CookieUtil.createTokenCookie(token);
    }

    public Cookie logout() {
        return CookieUtil.createLogoutCookie();
    }
}
