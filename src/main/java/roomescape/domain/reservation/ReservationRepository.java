package roomescape.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByMemberId(Long memberID);

    boolean existsByDateAndThemeAndTime(String date, Theme theme, Time time);
}
