package roomescape.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 글로벌

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원되지 않는 HTTP 메서드입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // 인증

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 비었습니다."),

    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),


    // 회원

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    INVALID_ROLE(HttpStatus.BAD_REQUEST, "역할이 존재하지 않습니다."),

    INVALID_EMAILPASSWORD(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 잘못되었습니다."),

    // 예약
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예약을 찾을 수 없습니다."),

    DUPLICATE_RESERVATION(HttpStatus.BAD_REQUEST, "중복 예약 대기입니다."),

    EXIST_RESERVATION(HttpStatus.CONFLICT, "이미 예약되었습니다."),

    WAITING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예약 대기를 찾을 수 없습니다."),

    // 테마
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 테마를 찾을 수 없습니다."),

    // 시간
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 시간을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
