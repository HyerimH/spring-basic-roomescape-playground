package roomescape.domain.reservation;

import static roomescape.global.exception.ErrorCode.THEME_NOT_FOUND;
import static roomescape.global.exception.ErrorCode.TIME_NOT_FOUND;
import static roomescape.global.exception.ErrorCode.USER_NOT_FOUND;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.dto.MyReservationResponse;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.Time;
import roomescape.domain.time.TimeRepository;
import roomescape.domain.waiting.WaitingRepository;
import roomescape.global.auth.LoginMember;
import roomescape.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public List<MyReservationResponse> findMyReservations(LoginMember member) {
        List<MyReservationResponse> reservationList = getReservationResponses(member.id());
        List<MyReservationResponse> waitingList = getWaitingResponses(member.id());

        return Stream.concat(reservationList.stream(), waitingList.stream()).toList();
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        Member member = findMemberOrThrow(loginMember.id());
        Time time = findValidTimeOrThrow(request.time());
        Theme theme = findValidThemeOrThrow(request.theme());

        String name = member.isAdmin() ? request.name() : loginMember.name();

        Reservation reservation = new Reservation(
                name,
                request.date(),
                time,
                theme,
                member
        );

        reservationRepository.save(reservation);

        return ReservationResponse.from(reservation);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private List<MyReservationResponse> getReservationResponses(Long memberId) {
        return reservationRepository.findByMemberId(memberId).stream()
                .map(reservation -> MyReservationResponse.from(reservation, "예약"))
                .toList();
    }

    private List<MyReservationResponse> getWaitingResponses(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId).stream()
                .map(waiting -> new MyReservationResponse(
                        waiting.waiting().getId(),
                        waiting.waiting().getTheme().getName(),
                        waiting.waiting().getDate(),
                        waiting.waiting().getTime().getValue(),
                        (waiting.rank() + 1) + "번째 예약대기"))
                .toList();
    }

    private Member findMemberOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private Time findValidTimeOrThrow(Long id) {
        Time time = timeRepository.findById(id)
                .orElseThrow(() -> new CustomException(TIME_NOT_FOUND));
        if (time.isDeleted()) {
            throw new CustomException(TIME_NOT_FOUND);
        }
        return time;
    }

    private Theme findValidThemeOrThrow(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new CustomException(THEME_NOT_FOUND));
        if (theme.isDeleted()) {
            throw new CustomException(THEME_NOT_FOUND);
        }
        return theme;
    }
}
