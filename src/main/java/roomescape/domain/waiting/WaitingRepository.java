package roomescape.domain.waiting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.domain.waiting.dto.WaitingWithRank;

@Repository
@RequiredArgsConstructor
public class WaitingRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public Waiting save(Waiting waiting) {
        if (waiting.getId() == null) {
            entityManager.persist(waiting);
        } else {
            entityManager.merge(waiting);
        }
        return waiting;
    }

    public List<Waiting> findByThemeIdAndDateAndTimeId(String date, Theme theme, Time time) {
        return entityManager.createQuery("SELECT w FROM Waiting w "+
                              "WHERE w.date = :date AND w.time = :time AND w.theme = :theme", Waiting.class)
                .setParameter("date",date)
                .setParameter("time", time)
                .setParameter("theme", theme)
                .getResultList();
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        return entityManager.createQuery("SELECT new roomescape.domain.waiting.dto.WaitingWithRank(w, " +
                                         "    (SELECT CAST(COUNT(w2) AS Long) " +
                                         "     FROM Waiting w2 " +
                                         "     WHERE w2.theme = w.theme " +
                                         "       AND w2.date = w.date " +
                                         "       AND w2.time = w.time " +
                                         "       AND w2.id < w.id)) " +
                                         "FROM Waiting w " +
                                         "WHERE w.member.id = :memberId", WaitingWithRank.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public Optional<Waiting> findById(Long id) {
        Waiting waiting = entityManager.find(Waiting.class, id);
        return Optional.ofNullable(waiting);
    }

    public void deleteById(Long id) {
        Waiting waiting = entityManager.find(Waiting.class, id);
        if (waiting != null) {
            entityManager.remove(waiting);
        }
    }
}
