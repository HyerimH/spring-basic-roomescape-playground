package roomescape.domain.reservation.dto;

import roomescape.domain.reservation.Reservation;

public record MyReservationResponse(
        Long reservationId,
        String theme,
        String date,
        String time,
        String status
) {

    public static MyReservationResponse from(Reservation reservation, String status
    ) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                status
        );
    }
}
