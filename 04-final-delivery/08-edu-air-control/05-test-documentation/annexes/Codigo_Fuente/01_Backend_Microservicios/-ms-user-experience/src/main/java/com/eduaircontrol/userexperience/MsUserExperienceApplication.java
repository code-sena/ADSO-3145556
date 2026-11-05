package com.eduaircontrol.userexperience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ms-user-experience: preferencias e interacciones del usuario.
 *
 * <p>Dueño de los datos de interaccion (preferencias, favoritos, ratings,
 * busquedas). No posee usuarios, ambientes ni variables: los referencia por UUID.
 */
@SpringBootApplication
public class MsUserExperienceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUserExperienceApplication.class, args);
    }
}
