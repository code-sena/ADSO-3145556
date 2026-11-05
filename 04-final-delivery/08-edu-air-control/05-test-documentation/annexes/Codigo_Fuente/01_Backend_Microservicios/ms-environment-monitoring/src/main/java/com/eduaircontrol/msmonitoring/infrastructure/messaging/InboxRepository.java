package com.eduaircontrol.msmonitoring.infrastructure.messaging;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InboxRepository extends JpaRepository<InboxMessage, UUID> {

    /**
     * Registra el mensaje solo si no estaba visto antes y dice si lo registro.
     *
     * <p>Usa {@code ON CONFLICT DO NOTHING} en lugar de depender de una excepcion
     * de clave primaria: en PostgreSQL un error aborta la transaccion entera, asi
     * que "intentar insertar y capturar la excepcion" dejaria el mensaje duplicado
     * en estado rollback. Devolver el numero de filas afectadas evita ese problema.
     */
    @Modifying
    @Query(value = """
            INSERT INTO environment_monitoring.inbox_message
                (message_id, consumer, message_type, received_at)
            VALUES (:messageId, :consumer, :messageType, now())
            ON CONFLICT (message_id) DO NOTHING
            """, nativeQuery = true)
    int claim(@Param("messageId") UUID messageId,
              @Param("consumer") String consumer,
              @Param("messageType") String messageType);
}