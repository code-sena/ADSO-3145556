package com.eduaircontrol.userexperience.searches.application;

import com.eduaircontrol.userexperience.searches.domain.model.Search;
import com.eduaircontrol.userexperience.searches.infrastructure.persistence.SearchJpaRepository;
import java.time.Clock;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Historial de busquedas del usuario.
 */
@Service
@RequiredArgsConstructor
public class SearchesService {

    private static final int MAX_LIMIT = 100;

    private final SearchJpaRepository searchRepository;
    private final Clock clock;

    @Transactional
    public Search record(UUID userId, String searchText, String appliedFilter) {
        requireUser(userId);
        return searchRepository.save(Search.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .searchText(searchText)
                .appliedFilter(appliedFilter)
                .searchedAt(clock.instant())
                .build());
    }

    @Transactional(readOnly = true)
    public List<Search> recent(UUID userId, int page, int limit) {
        requireUser(userId);
        Pageable pageable = PageRequest.of(
                Math.max(page, 1) - 1,
                Math.min(Math.max(limit, 1), MAX_LIMIT),
                Sort.by(Sort.Direction.DESC, "searchedAt"));
        return searchRepository.findByUserId(userId, pageable).getContent();
    }

    @Transactional
    public void delete(UUID userId, UUID searchId) {
        requireUser(userId);
        if (searchId == null) {
            throw new IllegalArgumentException("searchId es obligatorio");
        }
        searchRepository.deleteByIdAndUserId(searchId, userId);
    }

    private void requireUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId es obligatorio");
        }
    }
}
