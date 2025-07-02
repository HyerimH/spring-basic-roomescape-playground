package roomescape.domain.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.dto.MyReservationResponse;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.global.auth.LoginMember;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    public List<MyReservationResponse> findMyReservations(LoginMember member) {
        return reservationRepository.findByMemberId(member.id())
                .stream()
                .map(reservation -> MyReservationResponse.from(reservation, "예약"))
                .toList();
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {
        Member reservationMember = memberRepository.findById(member.id());
        if (reservationMember.isAdmin()) {
            reservationRequest = ReservationRequest.forAdmin(reservationRequest, reservationRequest.name());
        } else {
            reservationRequest = ReservationRequest.from(reservationRequest, member);
        }
        Reservation reservation = reservationRepository.save(reservationRequest, reservationMember);
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                reservation.getTime().getValue(), reservation.getTheme().getName());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue()))
                .toList();
    }
}
