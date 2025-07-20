package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.Time;
import roomescape.domain.member.Member;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.TimeRepository;


@Component
@Profile("test")
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) throws Exception {
        Member admin = memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        Member user = memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));

        Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        Time time1 = timeRepository.save(new Time("10:00"));
        Time time2 = timeRepository.save(new Time("12:00"));
        Time time3 = timeRepository.save(new Time("14:00"));
        Time time4 = timeRepository.save(new Time("16:00"));
        Time time5 = timeRepository.save(new Time("18:00"));
        Time time6 = timeRepository.save(new Time("20:00"));

        reservationRepository.save(new Reservation("", "2024-03-01", time1, theme1, admin));
        reservationRepository.save(new Reservation("", "2024-03-01", time2, theme2, admin));
        reservationRepository.save(new Reservation("", "2024-03-01", time3, theme3, admin));
        reservationRepository.save(new Reservation("브라운", "2024-03-01", time1, theme2));
    }
}
