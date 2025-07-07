package roomescape.domain.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Reservation> findAll() {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r " +
                        "JOIN r.theme t " +
                        "JOIN r.time ti",
                        Reservation.class)
                .getResultList();
    }

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            entityManager.persist(reservation);
        } else {
            entityManager.merge(reservation);
        }
        return reservation;
    }

    public void deleteById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (reservation != null) {
            entityManager.remove(reservation);
        }
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r " +
                        "JOIN r.theme t " +
                        "JOIN r.time ti " +
                        "WHERE r.date = :date AND t.id = :themeId",
                        Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r " +
                        "JOIN r.theme t " +
                        "JOIN r.time ti " +
                        "WHERE r.member.id = :memberId",
                        Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
