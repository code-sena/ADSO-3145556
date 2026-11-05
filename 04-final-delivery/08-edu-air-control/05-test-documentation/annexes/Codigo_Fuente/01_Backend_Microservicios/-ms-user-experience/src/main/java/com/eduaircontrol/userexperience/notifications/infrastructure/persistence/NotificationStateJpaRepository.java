package com.eduaircontrol.userexperience.notifications.infrastructure.persistence;

import com.eduaircontrol.userexperience.notifications.domain.model.NotificationState;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationStateJpaRepository
        extends JpaRepository<NotificationState, NotificationState.Pk> {

    List<NotificationState> findByUserId(UUID userId);

    List<NotificationState> findByUserIdAndAlertIdIn(UUID userId, Set<UUID> alertIds);
}
