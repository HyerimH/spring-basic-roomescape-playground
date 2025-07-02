package roomescape.domain.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThemeRespository {

    @PersistenceContext
    private EntityManager entityManager;


    public List<Theme> findAll() {
        return entityManager.createQuery("SELECT t FROM Theme t where t.deleted = false",
                        Theme.class)
                .getResultList();
    }

    public Theme save(Theme theme) {
        if (theme.getId() == null) {
            entityManager.persist(theme);
        } else {
            entityManager.merge(theme);
        }
        return theme;
    }

    public void deleteById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        if(theme == null){
            theme.delete();
            entityManager.merge(theme);
        }
    }
}
