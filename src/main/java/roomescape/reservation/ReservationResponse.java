package roomescape.reservation;

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
                reservation.getTheme().toString(),
                reservation.getDate(),
                reservation.getTime().toString()
        );
    }
}
