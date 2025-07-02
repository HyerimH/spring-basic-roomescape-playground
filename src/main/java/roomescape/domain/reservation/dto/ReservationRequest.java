package roomescape.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import roomescape.global.auth.LoginMember;

public record ReservationRequest(
        String name,

        @NotNull(message = "예약 날짜는 필수입니다.")
        String date,

        @NotNull(message = "테마는 필수입니다.")
        Long theme,

        @NotNull(message = "예약 시간은 필수입니다.")
        Long time
) {

    public static ReservationRequest from(ReservationRequest request, LoginMember member) {
        return new ReservationRequest(
                member.name(),
                request.date(),
                request.theme(),
                request.time()
        );
    }

    public static ReservationRequest forAdmin(ReservationRequest request, String name) {
        return new ReservationRequest(
                name,
                request.date(),
                request.theme(),
                request.time()
        );
    }
}
