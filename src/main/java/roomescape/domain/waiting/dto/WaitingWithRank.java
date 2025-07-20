package roomescape.domain.waiting.dto;

import roomescape.domain.waiting.Waiting;

public record WaitingWithRank(
        Waiting waiting,
        Long rank
) {

}
