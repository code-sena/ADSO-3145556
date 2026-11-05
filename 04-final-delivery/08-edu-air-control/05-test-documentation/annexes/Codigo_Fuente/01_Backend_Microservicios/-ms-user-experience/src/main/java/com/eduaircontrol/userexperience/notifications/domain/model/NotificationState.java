package com.eduaircontrol.userexperience.notifications.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Estado de lectura de una notificacion por usuario y alerta.
 *
 * <p>La alerta vive en ms-environment-monitoring; aqui solo se guarda la
 * interaccion del usuario con ella.
 */
@Entity
@Table(name = "notification_state", schema = "user_experience")
@IdClass(NotificationState.Pk.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationState {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "alert_id")
    private UUID alertId;

    @Column(name = "read_at", nullable = false)
    private Instant readAt;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Pk implements Serializable {
        private UUID userId;
        private UUID alertId;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Pk other)) {
                return false;
            }
            return Objects.equals(userId, other.userId) && Objects.equals(alertId, other.alertId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, alertId);
        }
    }
}
