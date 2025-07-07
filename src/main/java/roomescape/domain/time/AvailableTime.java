package roomescape.domain.time;

public record AvailableTime (
    Long timeId,
    String time,
    boolean booked
){

}
