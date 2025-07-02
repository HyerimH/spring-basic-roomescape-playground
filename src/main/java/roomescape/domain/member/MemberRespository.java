package roomescape.domain.member;

import static roomescape.global.exception.ErrorCode.INVALID_EMAILPASSWORD;
import static roomescape.global.exception.ErrorCode.USER_NOT_FOUND;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.NoSuchElementException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.global.exception.CustomException;

@Repository
public class MemberRespository {

    @PersistenceContext
    private EntityManager entityManager;

    public Member save(Member member) {
        if (member.getId() == null) {
            entityManager.persist(member);
        } else {
            member = entityManager.merge(member);
        }
        return member;
    }

    public Member findByEmailAndPassword(String email, String password) {
        try {
            return entityManager.createQuery(
                            "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password", Member.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException(INVALID_EMAILPASSWORD);
        }
    }

    public Member findByName(String name) {
        try {
           return entityManager.createQuery(
                   "SELECT m FROM Member m WHERE m.name = :name", Member.class)
                   .setParameter("name", name)
                   .getSingleResult();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException(USER_NOT_FOUND);
        }
    }

    public Member findById(Long id) {
      Member member = entityManager.find(Member.class, id);
      if(member == null){
          throw new CustomException(USER_NOT_FOUND);
      }
      return member;
    }
}
