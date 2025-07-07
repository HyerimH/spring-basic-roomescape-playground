package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.Time;
import roomescape.domain.time.TimeRepository;

@DataJpaTest
@ContextConfiguration(classes = {TimeRepository.class, ReservationRepository.class, MemberRepository.class,
        ThemeRepository.class, RoomescapeApplication.class})
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Test
    @DisplayName("Time가 정상적으로 저장되고 조회된다")
    void 사단계() {
        Time time = new Time("10:00");
        entityManager.persist(time);
        entityManager.flush();

        Optional<Time> persistTime = timeRepository.findById(time.getId());

        assertThat(persistTime.get().getValue()).isEqualTo(time.getValue());
    }
}

