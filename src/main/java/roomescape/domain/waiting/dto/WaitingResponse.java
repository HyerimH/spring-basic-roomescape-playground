package roomescape.domain.waiting.dto;

import roomescape.domain.waiting.Waiting;

public record WaitingResponse(
        Long id,
        String theme,
        String time,
        String date,
        Long waitingNumber
) {
    public static WaitingResponse from(Waiting waiting, String themeName, String timeValue, Long waitingNumber) {
        return new WaitingResponse(
                waiting.getId(),
                themeName,
                timeValue,
                waiting.getDate(),
                waitingNumber
        );
    }
}

