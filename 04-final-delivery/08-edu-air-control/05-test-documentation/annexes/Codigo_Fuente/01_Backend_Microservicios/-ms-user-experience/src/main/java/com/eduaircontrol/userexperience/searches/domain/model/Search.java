package com.eduaircontrol.userexperience.searches.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Busqueda realizada por un usuario, con el filtro aplicado.
 */
@Entity
@Table(name = "searches", schema = "user_experience")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Search {

    @Id
    @Column(name = "search_id")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "search_text", length = 500)
    private String searchText;

    @Column(name = "applied_filter", length = 500)
    private String appliedFilter;

    @Column(name = "searched_at", nullable = false, updatable = false)
    private Instant searchedAt;
}
