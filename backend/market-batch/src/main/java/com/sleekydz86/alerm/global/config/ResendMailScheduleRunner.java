package com.sleekydz86.alerm.global.config;

import com.sleekydz86.alerm.batch.application.ScheduleCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@ConditionalOnProperty(name = "schedule.resend-mail", havingValue = "true")
@Configuration
public class ResendMailScheduleRunner implements ApplicationRunner {

    private final ScheduleCreateService scheduleCreateService;

    @Override
    public void run(final ApplicationArguments args) {
        scheduleCreateService.resendMail();
    }
}
