package roomescape.domain.member;

public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;

    public Member(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = Role.from(role);
    }

    public Member(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.from(role);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role.name();
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }
}
