package com.eduaircontrol.userexperience.shared.contract;

import java.util.Optional;
import java.util.UUID;

/**
 * Identidad del solicitante.
 *
 * <p>El servicio no tiene usuarios: el ID viaja en el JWT (claim {@code userId}).
 */
public interface UserIdentityPort {

    /** UUID del usuario del token actual, o vacio si el token no lo trae. */
    Optional<UUID> currentUserId();

    /** Correo del usuario del token actual. */
    Optional<String> currentEmail();
}
