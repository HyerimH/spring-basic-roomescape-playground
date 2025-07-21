package jwt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class AuthConfig {

    @Bean
    public JwtUtils jwtUtils(JwtProperties properties) {
        return new JwtUtils(properties.secret(), properties.expiration());
    }
}
