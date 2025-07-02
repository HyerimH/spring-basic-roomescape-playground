package roomescape.domain.member;

import static roomescape.global.exception.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.member.dto.MemberRequest;
import roomescape.domain.member.dto.MemberResponse;
import roomescape.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member getMemberById(Long id){
        Member member = memberRepository.findById(id);
        if(member == null){
            throw new CustomException(USER_NOT_FOUND);
        }
        return member;
    }
}
