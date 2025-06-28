package roomescape.global.auth;

public record LoginMember(Long id, String name, String email, String role) {

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}
