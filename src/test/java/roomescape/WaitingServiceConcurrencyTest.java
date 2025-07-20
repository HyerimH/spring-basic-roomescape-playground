package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
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
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.Time;
import roomescape.domain.time.TimeRepository;
import roomescape.domain.waiting.WaitingRepository;
import roomescape.domain.waiting.WaitingService;
import roomescape.domain.waiting.dto.WaitingRequest;
import roomescape.domain.waiting.dto.WaitingWithRank;

@SpringBootTest
@ActiveProfiles("test")
class WaitingServiceConcurrencyTest {

    @Autowired
    private WaitingService waitingService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WaitingRepository waitingRepository;

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
        waitingRepository.deleteAllInBatch();
        reservationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        themeRepository.deleteAllInBatch();
        timeRepository.deleteAllInBatch();

        member = memberRepository.save(new Member("name", "test@test.com", "password", "USER"));
        theme = themeRepository.save(new Theme("공포테마", "thumbnail.jpg"));
        time = timeRepository.save(new Time("10:00"));
        loginMember = new LoginMember(member.getId(), member.getName(), member.getEmail(), "USER");
    }

    @Test
    @DisplayName("동시에 여러 명이 웨이팅 신청해도 모두 정상적으로 저장되고 순서대로 등수가 매겨져야 한다.")
    void testWaitingOrderingWithConcurrency() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        String date = LocalDate.now().plusDays(1).toString();
        WaitingRequest request = new WaitingRequest(date, theme.getId(), time.getId());

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    waitingService.save(request, loginMember);
                } catch (Exception e) {
                    System.out.println("Fail: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // then
        assertThat(waitingRepository.count()).isEqualTo(1);
    }
}

