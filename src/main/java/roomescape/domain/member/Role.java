package roomescape.domain.member;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자"),
    USER("일반 회원");

    private final String description;

    public static Role from(String role){
        return Arrays.stream(values())
                .filter(roleName->roleName.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ROLE));
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
