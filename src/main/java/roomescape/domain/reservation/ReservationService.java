package roomescape.domain.reservation;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.global.auth.LoginMember;

@Service
public class ReservationService {

    private ReservationRespository reservationRespository;

    public ReservationService(ReservationRespository reservationRespository) {
        this.reservationRespository = reservationRespository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {
        if (reservationRequest.name() == null && member != null) {
            reservationRequest = ReservationRequest.from(member, reservationRequest);
        }

        Reservation reservation = reservationRespository.save(reservationRequest);

        return ReservationResponse.from(reservation);
    }

    public void deleteById(Long id) {
        reservationRespository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRespository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue()))
                .toList();
    }
}
