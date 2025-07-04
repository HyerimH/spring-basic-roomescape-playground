package roomescape.domain.reservation;

import static roomescape.global.exception.ErrorCode.THEME_NOT_FOUND;
import static roomescape.global.exception.ErrorCode.TIME_NOT_FOUND;
import static roomescape.global.exception.ErrorCode.USER_NOT_FOUND;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
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

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                reservation.getTime().getValue(), reservation.getTheme().getName());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()))
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
