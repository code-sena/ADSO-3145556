package com.eduaircontrol.msmonitoring.shared.contract;

import java.util.UUID;

/**
 * Identidad del solicitante.
 *
 * <p>El servicio no tiene usuarios: el ID viaja en el JWT (claim {@code userId}),
 * que es lo que el gateway propaga. Por eso el contrato solo necesita "el usuario
 * de este token", no un directorio.
 */
public interface UserIdentityPort {

    /** UUID del usuario del token actual, o vacio si el token no lo trae. */
    java.util.Optional<UUID> currentUserId();

    /** Correo del usuario del token actual. */
    java.util.Optional<String> currentEmail();
}