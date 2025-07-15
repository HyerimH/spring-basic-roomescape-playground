package roomescape.domain.reservation;

import io.micrometer.common.lang.NonNullApi;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

@NonNullApi
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.theme " +
           "JOIN FETCH r.time " +
           "WHERE r.date = :date AND r.theme.id = :themeId")
    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.theme " +
           "JOIN FETCH r.time " +
           "JOIN FETCH r.member " +
           "WHERE r.member.id = :memberId")
    List<Reservation> findByMemberId(Long memberId);

    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.theme " +
           "JOIN FETCH r.time " +
           "JOIN FETCH r.member " +
           "WHERE r.id = :id")
    Optional<Reservation> findById(Long id);

    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.theme " +
           "JOIN FETCH r.time " +
           "JOIN FETCH r.member")
    List<Reservation> findAll();

    boolean existsByDateAndThemeAndTime(String date, Theme theme, Time time);
}
