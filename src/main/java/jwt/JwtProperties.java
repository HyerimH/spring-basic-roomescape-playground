package jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "roomescape.auth.jwt")
public record JwtProperties(String secret, long expiration) {
}
