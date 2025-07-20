package roomescape.auth;

import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public record LoginMember(
        Long id,
        String name,
        String email,
        String role
) {
    public static LoginMember from (Member member) {
        return new LoginMember(
                member.getId(), member.getName(), member.getEmail(), member.getRole()
        );
    }

    public boolean isAdmin() {
        return Role.from(role).isAdmin();
    }
}
