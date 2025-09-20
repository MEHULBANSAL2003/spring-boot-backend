package com.springbootBackend.backend.service.cronService;

import com.springbootBackend.backend.entity.UserDataEntity;
import com.springbootBackend.backend.repository.UserDataRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserInactivityScheduler {

    private static final Logger logger = LoggerFactory.getLogger(UserInactivityScheduler.class);

    @Autowired
    UserDataRepository userDataRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void markStatusInactive(){

        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(30);
        List<UserDataEntity> inactiveUsers = userDataRepository.findByCurrStatusAndLastLoginBefore(
                UserDataEntity.userStatus.ACTIVE,
                thresholdDate
        );

        for (UserDataEntity user : inactiveUsers) {
            user.setCurrStatus(UserDataEntity.userStatus.INACTIVE);
        }

        userDataRepository.saveAll(inactiveUsers);
    }

}
