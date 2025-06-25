package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberDao memberDao;
    private final String secretKey;

    public MemberService(MemberDao memberDao,
            @Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.memberDao = memberDao;
        this.secretKey = secretKey;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public MemberResponse checkLogin(String token) {
        try {
            Long memberId = Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
            Member member = memberDao.findById(memberId);
            return new MemberResponse(member.getName());
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
}
