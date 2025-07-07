package roomescape.domain.waiting.dto;

import roomescape.domain.waiting.Waiting;

public record WaitingResponse(
        Long id,
        String theme,
        String time,
        String date,
        Long waitingNumber
) {

}

