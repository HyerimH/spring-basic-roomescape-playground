package roomescape.domain.reservation;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.dto.MyReservationResponse;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.waiting.WaitingRepository;
import roomescape.global.auth.LoginMember;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public List<MyReservationResponse> findMyReservations(LoginMember member) {
        List<MyReservationResponse> reservationList = reservationRepository.findByMemberId(member.id())
                .stream()
                .map(reservation -> MyReservationResponse.from(reservation, "예약"))
                .toList();

        List<MyReservationResponse> waitingList = waitingRepository.findWaitingsWithRankByMemberId(member.id())
                .stream()
                .map(it -> new MyReservationResponse(
                                it.waiting().getId(),
                                it.waiting().getTheme().getName(),
                                it.waiting().getDate(),
                                it.waiting().getTime().getValue(),
                        (it.rank() + 1) + "번째 예약대기"))
                .toList();

        return Stream.concat(reservationList.stream(), waitingList.stream()).toList();
    }

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
