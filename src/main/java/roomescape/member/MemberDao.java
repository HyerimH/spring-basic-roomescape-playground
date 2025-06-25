package roomescape.member;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("role")
    );

    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("INSERT INTO member(name, email, password, role) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());
            ps.setString(4, member.getRole());
            return ps;
        }, keyHolder);

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getEmail(), "USER");
    }

    public Member findByEmailAndPassword(String email, String password) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE email = ? AND password = ?",
                    MEMBER_ROW_MAPPER,
                    email, password
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }

    public Member findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE name = ?",
                    MEMBER_ROW_MAPPER,
                    name
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("해당 이름을 가진 Member를 찾을 수 없습니다. Name: " + name);
        }
    }

    public Member findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE id = ?",
                    MEMBER_ROW_MAPPER,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("해당 Member를 찾을 수 없습니다. ID: " + id);
        }
    }
}
