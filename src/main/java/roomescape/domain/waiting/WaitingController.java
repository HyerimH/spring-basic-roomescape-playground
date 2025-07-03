package roomescape.domain.waiting;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.waiting.dto.WaitingRequest;
import roomescape.domain.waiting.dto.WaitingResponse;
import roomescape.global.auth.AuthenticatedMember;
import roomescape.global.auth.LoginMember;

@RestController
@RequestMapping("/waitings")
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService waitingService;

    @PostMapping
    public ResponseEntity<WaitingResponse> create(@Valid  @RequestBody WaitingRequest waitingRequest,
            @AuthenticatedMember LoginMember loginMember) {
        WaitingResponse waitingResponse = waitingService.save(waitingRequest, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.id())).body(waitingResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
