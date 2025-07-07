package roomescape.domain.member;

import static roomescape.exception.ErrorCode.INVALID_EMAILPASSWORD;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.exception.CustomException;

@Repository
public class MemberRepository {

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
        return entityManager.createQuery(
                        "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password", Member.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Member.class, id));
    }
}
