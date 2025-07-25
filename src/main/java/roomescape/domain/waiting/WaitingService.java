package roomescape.domain.waiting;

import static roomescape.exception.ErrorCode.DUPLICATE_RESERVATION;
import static roomescape.exception.ErrorCode.FORBIDDEN;
import static roomescape.exception.ErrorCode.THEME_NOT_FOUND;
import static roomescape.exception.ErrorCode.TIME_NOT_FOUND;
import static roomescape.exception.ErrorCode.USER_NOT_FOUND;
import static roomescape.exception.ErrorCode.WAITING_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.Time;
import roomescape.domain.time.TimeRepository;
import roomescape.domain.waiting.dto.WaitingRequest;
import roomescape.domain.waiting.dto.WaitingResponse;
import roomescape.domain.waiting.dto.WaitingWithRank;
import roomescape.auth.LoginMember;
import roomescape.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    @Transactional
    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Theme theme = themeRepository.findById(waitingRequest.theme())
                .orElseThrow(() -> new CustomException(THEME_NOT_FOUND));
        Time time = timeRepository.findById(waitingRequest.time())
                .orElseThrow(() -> new CustomException(TIME_NOT_FOUND));

        boolean isDuplicate = waitingRepository.existsByMemberIdAndThemeIdAndDateAndTimeId(
                member.getId(), theme.getId(), waitingRequest.date(), time.getId());

        if (isDuplicate) {
            throw new CustomException(DUPLICATE_RESERVATION);
        }

        Waiting waiting = new Waiting(member, time, theme, waitingRequest.date());

        try {
            waitingRepository.save(waiting);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(DUPLICATE_RESERVATION);
        }

        List<WaitingWithRank> waitingsWithRank = waitingRepository.findWaitingsWithRankByMemberId(member.getId());
        Long waitingNumber = (long) waitingsWithRank.size();

        return new WaitingResponse(waiting.getId(), theme.getName(), time.getValue(), waiting.getDate(), waitingNumber);
    }

    @Transactional
    public void deleteById(Long waitingId, Long loginMemberId) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(()-> new CustomException(WAITING_NOT_FOUND));

        if(!waiting.getMember().getId().equals(loginMemberId)){
            throw new CustomException(FORBIDDEN);
        }

        waitingRepository.deleteById(waitingId);
    }
}
