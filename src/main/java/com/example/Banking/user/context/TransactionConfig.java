package com.example.Banking.user.context;

import com.example.Banking.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
@Configuration
@EnableJpaAuditing(auditorAwareRef = "userContextProvider", dateTimeProviderRef = "dateTimeProvider")
public class TransactionConfig {

    @Bean
    UserContextService userContextService(UserRepository userRepository){
        return new SpringSecurityUserContextService(userRepository);
    }


    @Bean
    DateTimeService dateTimeService(){
        return new CurrentDateTimeService();
    }

    @Bean
    DateTimeProvider dateTimeProvider(DateTimeService dateTimeService){
        return () -> Optional.of(dateTimeService.getCurrentDateTime());
    }

    @Bean
    AuditorAware<String> userContextProvider(UserContextService userContextService){
        return () -> Optional.of(userContextService.getCurrentUsername());
    }
}


