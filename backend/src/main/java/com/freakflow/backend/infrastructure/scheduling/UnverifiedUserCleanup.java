package com.freakflow.backend.infrastructure.scheduling;

import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.UserRepository;
import com.freakflow.backend.domain.repository.VerificationTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class UnverifiedUserCleanup {

    private final UserRepository userRepo;
    private final VerificationTokenRepository tokenRepo;

    public UnverifiedUserCleanup(UserRepository userRepo,
                                 VerificationTokenRepository tokenRepo) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
    }

    /** каждую ночь в полночь */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void purgeStaleUsers() {
        Instant cutoff = Instant.now().minus(24, ChronoUnit.HOURS);
        List<User> stale = userRepo.findByEnabledFalseAndCreatedAtBefore(cutoff);
        for (User u : stale) {
            tokenRepo.deleteByUser(u);
            userRepo.delete(u);
        }
    }
}
