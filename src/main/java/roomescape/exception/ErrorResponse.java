package roomescape.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final ErrorCode errorCode;
    private final String errorMessage;
}
