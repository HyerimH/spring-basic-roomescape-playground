package roomescape.domain.member;

import static roomescape.global.exception.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private MemberDao memberDao;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member getMemberById(Long id){
        Member member = memberDao.findById(id);
        if(member == null){
            throw new CustomException(USER_NOT_FOUND);
        }
        return member;
    }
}
