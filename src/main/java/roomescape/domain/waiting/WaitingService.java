package roomescape.domain.waiting;

import static roomescape.global.exception.ErrorCode.DUPLICATE_RESERVATION;

import java.util.List;
import lombok.RequiredArgsConstructor;
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
import roomescape.global.auth.LoginMember;
import roomescape.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    @Transactional
    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.id());

        Theme theme = themeRepository.findById(waitingRequest.theme());
        Time time = timeRepository.findById(waitingRequest.time());

        List<Waiting> existingWaitings = waitingRepository.findByThemeIdAndDateAndTimeId(
                waitingRequest.date(), theme, time);

        if (!existingWaitings.isEmpty()) {
            throw new CustomException(DUPLICATE_RESERVATION);
        }

        Waiting waiting = new Waiting(member, time, theme, waitingRequest.date());
        waitingRepository.save(waiting);

        List<WaitingWithRank> waitingsWithRank = waitingRepository.findWaitingsWithRankByMemberId(member.getId());
        Long waitingNumber = (long) waitingsWithRank.size();

        return new WaitingResponse(waiting.getId(), theme.getName(), time.getValue(), waiting.getDate(), waitingNumber);
    }

    @Transactional
    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
