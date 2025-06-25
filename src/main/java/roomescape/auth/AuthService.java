package roomescape.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return jwtTokenProvider.createToken(member);
    }

    public LoginMember checkLogin(String token) {
        try {
            Long memberId = jwtTokenProvider.extractMemberId(token);
            Member member = memberDao.findById(memberId);
            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}
