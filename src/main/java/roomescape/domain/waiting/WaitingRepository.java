package roomescape.domain.waiting;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.domain.waiting.dto.WaitingWithRank;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findByThemeIdAndDateAndTimeId(Long themeId, String date, Long timeId);

    @Query("SELECT new roomescape.domain.waiting.dto.WaitingWithRank(w, " +
           "    (SELECT CAST(COUNT(w2) AS Long) " +
           "     FROM Waiting w2 " +
           "     WHERE w2.theme = w.theme " +
           "       AND w2.date = w.date " +
           "       AND w2.time = w.time " +
           "       AND w2.id < w.id)) " +
           "FROM Waiting w " +
           "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);
}
