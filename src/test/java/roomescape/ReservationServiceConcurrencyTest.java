package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import roomescape.auth.LoginMember;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.ReservationService;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.Time;
import roomescape.domain.time.TimeRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TimeRepository timeRepository;

    private Member member;
    private Theme theme;
    private Time time;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        // 테스트 실행 전 기존 데이터 정리
        reservationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        themeRepository.deleteAllInBatch();
        timeRepository.deleteAllInBatch();

        // 테스트에 필요한 기본 데이터 설정
        member = memberRepository.save(new Member("name", "test@test.com", "password", "USER"));
        theme = themeRepository.save(new Theme(5L, "공포테마", "thumbnail.jpg"));
        time = timeRepository.save(new Time("10:00"));
        loginMember = new LoginMember(member.getId(), member.getName(), member.getEmail(), "USER");
    }

    @Test
    @DisplayName("동시에 10개의 예약 요청이 들어올 경우, 단 1개의 예약만 성공해야 한다.")
    void testReservationConcurrency() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        LocalDate date = LocalDate.now().plusDays(1);
        ReservationRequest request = new ReservationRequest(member.getName(), date.toString(), theme.getId(), time.getId());

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.save(request, loginMember);
                } catch (Exception e) {
                    // 동시성 문제로 인해 발생하는 예외(DataIntegrityViolationException 등)는 무시
                    System.out.println("Reservation failed for a thread: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 작업을 완료할 때까지 대기
        executorService.shutdown();

        // then
        // 오직 1개의 예약만 데이터베이스에 저장되어야 한다.
        assertThat(reservationRepository.count()).isEqualTo(1L);
    }
}

