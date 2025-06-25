package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.member.LoginMember;
import roomescape.member.MemberResponse;

@Service
public class ReservationService {

    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {
        if (reservationRequest.name() == null && member != null) {
            reservationRequest = ReservationRequest.from(member, reservationRequest);
        }

        Reservation reservation = reservationDao.save(reservationRequest);

        return ReservationResponse.from(reservation);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue()))
                .toList();
    }
}
