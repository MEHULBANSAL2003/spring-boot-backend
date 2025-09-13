package com.springbootBackend.backend.service.cronService;

import com.springbootBackend.backend.repository.UserPendingVerificationRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PendingUserCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(PendingUserCleanupService.class);

    @Autowired
    private UserPendingVerificationRepository userPendingVerificationRepository;


    @Scheduled(cron = "0 */10 * * * ?")
    @Transactional
    public void deleteExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        try {
            int deletedCount = userPendingVerificationRepository.deleteAllByOtpExpiryTimeBefore(now);
        }
        catch(Exception e){
        }
    }
}
