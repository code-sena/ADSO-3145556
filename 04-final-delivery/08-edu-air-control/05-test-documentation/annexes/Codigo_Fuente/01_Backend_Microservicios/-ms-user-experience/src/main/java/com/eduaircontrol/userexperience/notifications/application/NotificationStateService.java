package com.eduaircontrol.userexperience.notifications.application;

import com.eduaircontrol.userexperience.notifications.domain.model.NotificationState;
import com.eduaircontrol.userexperience.notifications.infrastructure.persistence.NotificationStateJpaRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Estado de lectura de notificaciones. Idempotente: marcar dos veces no duplica.
 */
@Service
@RequiredArgsConstructor
public class NotificationStateService {

    private final NotificationStateJpaRepository notificationStateRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public Set<UUID> readAlertIds(UUID userId) {
        if (userId == null) {
            return Set.of();
        }
        return notificationStateRepository.findByUserId(userId).stream()
                .map(NotificationState::getAlertId)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void markRead(UUID userId, UUID alertId) {
        if (userId == null || alertId == null) {
            return;
        }
        notificationStateRepository.findById(new NotificationState.Pk(userId, alertId))
                .orElseGet(() -> notificationStateRepository.save(
                        new NotificationState(userId, alertId, clock.instant())));
    }

    @Transactional
    public void markAllRead(UUID userId, Set<UUID> alertIds) {
        if (userId == null || alertIds == null || alertIds.isEmpty()) {
            return;
        }
        alertIds.forEach(alertId -> markRead(userId, alertId));
    }

    @Transactional(readOnly = true)
    public Instant readAt(UUID userId, UUID alertId) {
        if (userId == null || alertId == null) {
            return null;
        }
        return notificationStateRepository.findById(new NotificationState.Pk(userId, alertId))
                .map(NotificationState::getReadAt)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Map<UUID, Instant> readAtByAlerts(UUID userId, Set<UUID> alertIds) {
        if (userId == null || alertIds == null || alertIds.isEmpty()) {
            return Map.of();
        }
        return notificationStateRepository.findByUserIdAndAlertIdIn(userId, alertIds).stream()
                .collect(Collectors.toMap(NotificationState::getAlertId, NotificationState::getReadAt,
                        (first, second) -> first));
    }
}
