package roomescape.domain.reservation.dto;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.waiting.dto.WaitingWithRank;

public record MyReservationResponse(
        Long id,
        String theme,
        String date,
        String time,
        String status
) {

    private final static String RESERVATION_STATUS = "예약";
    private final static String WAITING_STATUS = "번째 예약대기";

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                RESERVATION_STATUS
        );
    }

    public static MyReservationResponse from(WaitingWithRank waitingWithRank) {
        return new MyReservationResponse(
                waitingWithRank.waiting().getId(),
                waitingWithRank.waiting().getTheme().getName(),
                waitingWithRank.waiting().getDate(),
                waitingWithRank.waiting().getTime().getValue(),
                waitingWithRank.rank() + 1 + WAITING_STATUS
        );
    }
}
