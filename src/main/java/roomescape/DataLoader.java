package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;


    @Override
    public void run(String... args) throws Exception {
        memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));
    }
}
