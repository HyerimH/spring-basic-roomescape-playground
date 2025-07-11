package roomescape.auth;

import static roomescape.exception.ErrorCode.INVALID_EMAILPASSWORD;
import static roomescape.exception.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.exception.CustomException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new CustomException(INVALID_EMAILPASSWORD));
        return jwtTokenProvider.createToken(member);
    }
}
