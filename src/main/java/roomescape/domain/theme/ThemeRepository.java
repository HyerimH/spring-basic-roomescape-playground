package roomescape.domain.theme;

import static roomescape.global.exception.ErrorCode.THEME_NOT_FOUND;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import roomescape.global.exception.CustomException;

@Repository
public class ThemeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Theme findById(Long id){
        Theme theme = entityManager.find(Theme.class, id);
        if(theme == null){
            throw new CustomException(THEME_NOT_FOUND);
        }
        return theme;
    }

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
