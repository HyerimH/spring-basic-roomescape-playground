package roomescape.domain.reservation.dto;

import roomescape.domain.reservation.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        String theme,
        String date,
        String time
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }
}
