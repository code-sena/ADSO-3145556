package com.eduaircontrol.userexperience.preferences.infrastructure.persistence;

import com.eduaircontrol.userexperience.preferences.domain.model.UserPreference;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceJpaRepository extends JpaRepository<UserPreference, UUID> {

    Optional<UserPreference> findByUserId(UUID userId);
}
