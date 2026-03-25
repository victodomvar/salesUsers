package com.salesusers.usermanagement.infrastructure.config;

import com.salesusers.usermanagement.application.port.in.CreateUserUseCase;
import com.salesusers.usermanagement.application.port.out.UserPersistencePort;
import com.salesusers.usermanagement.application.usecase.CreateUserService;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserPersistencePort userPersistencePort, Clock clock) {
        return new CreateUserService(userPersistencePort, clock);
    }
}
