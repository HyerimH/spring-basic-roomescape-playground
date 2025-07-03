package roomescape.domain.time;

import static roomescape.global.exception.ErrorCode.TIME_NOT_FOUND;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import roomescape.global.exception.CustomException;

@Repository
public class TimeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Time findById(Long id) {
        Time time = entityManager.find(Time.class, id);
        if(time == null){
            throw new CustomException(TIME_NOT_FOUND);
        }
        return time;
    }

    public List<Time> findAll() {
        return entityManager.createQuery("SELECT t FROM Time t WHERE t.deleted = false", Time.class)
                .getResultList();
    }

    public Time save(Time time) {
        if (time.getId() == null) {
            entityManager.persist(time);
        } else {
            entityManager.merge(time);
        }
        return time;
    }

    public void deleteById(Long id) {
        Time time = entityManager.find(Time.class, id);
        if (time != null) {
            time.delete();
            entityManager.merge(time);
        }
    }
}
