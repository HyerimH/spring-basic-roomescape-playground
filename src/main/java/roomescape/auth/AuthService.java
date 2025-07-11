package roomescape.auth;

import static roomescape.exception.ErrorCode.INVALID_EMAILPASSWORD;

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
    private final BCryptPasswordEncoder passwordEncoder;

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new CustomException(INVALID_EMAILPASSWORD));
        if(!passwordEncoder.matches(loginRequest.password(), member.getPassword())){
            throw new CustomException(INVALID_EMAILPASSWORD);
        }
        return jwtTokenProvider.createToken(member);
    }
}
