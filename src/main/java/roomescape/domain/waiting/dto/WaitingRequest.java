package roomescape.domain.waiting.dto;

import jakarta.validation.constraints.NotNull;

public record WaitingRequest(
        @NotNull(message = "예약 날짜는 필수입니다.")
        String date,

        @NotNull(message = "테마는 필수입니다.")
        Long theme,

        @NotNull(message = "예약 시간은 필수입니다.")
        Long time
) {

}

