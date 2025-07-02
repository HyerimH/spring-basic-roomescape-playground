package roomescape.domain.member.dto;

public record MemberRequest(
        String name,
        String email,
        String password
) {

}
