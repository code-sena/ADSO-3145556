package com.eduaircontrol.msmonitoring.infrastructure.messaging;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Marca de un mensaje ya procesado.
 *
 * <p>La garantia del outbox es at-least-once, asi que el mismo evento puede
 * llegar mas de una vez: si el broker confirma y el monolito se cae antes de
 * marcar el evento como publicado, el relay lo reenvia. Sin esta tabla, esa
 * repeticion duplicaria mediciones en el read model.
 *
 * <p>Se escribe en la <b>misma transaccion</b> que las mediciones del mensaje. Por
 * eso el registro y los datos son consistentes entre si: nunca queda registrado
 * un mensaje cuyas mediciones no se guardaron.
 */
@Entity
@Table(name = "inbox_message", schema = "environment_monitoring")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboxMessage {

    @Id
    @Column(name = "message_id")
    private UUID messageId;

    @Column(nullable = false, length = 60)
    private String consumer;

    @Column(name = "message_type", nullable = false, length = 80)
    private String messageType;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;
}