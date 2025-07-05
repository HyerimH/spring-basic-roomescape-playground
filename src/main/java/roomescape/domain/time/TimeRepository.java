package roomescape.domain.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TimeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Time> findById(Long id){
        return entityManager.createQuery("SELECT t FROM Time t WHERE t.id = :id AND t.deleted = false",
                        Time.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
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
