package roomescape.domain.waiting.dto;

public record WaitingResponse(
        Long id,
        String theme,
        String time,
        String date,
        Long waitingNumber
) {

}

