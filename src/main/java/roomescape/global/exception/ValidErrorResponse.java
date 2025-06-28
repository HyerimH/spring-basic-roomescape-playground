package roomescape.global.exception;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidErrorResponse {

    private final String errorCode;
    private final String errorMessage;
    private final Map<String, String> validation;
}
