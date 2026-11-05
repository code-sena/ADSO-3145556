package com.eduaircontrol.userexperience.searches.infrastructure.persistence;

import com.eduaircontrol.userexperience.searches.domain.model.Search;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchJpaRepository extends JpaRepository<Search, UUID> {

    List<Search> findByUserIdOrderBySearchedAtDesc(UUID userId);

    Page<Search> findByUserId(UUID userId, Pageable pageable);

    void deleteByIdAndUserId(UUID id, UUID userId);
}
