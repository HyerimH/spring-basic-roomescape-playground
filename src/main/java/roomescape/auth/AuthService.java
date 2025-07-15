package roomescape.auth;

import static roomescape.exception.ErrorCode.INVALID_EMAILPASSWORD;

import jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.exception.CustomException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new CustomException(INVALID_EMAILPASSWORD));
        return jwtUtils.createToken(member);
    }
}
