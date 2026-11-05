package com.eduaircontrol.msclassroom.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eduaircontrol.msclassroom.domain.port.out.CampusRepository;
import com.eduaircontrol.msclassroom.shared.exception.ConflictException;
import com.eduaircontrol.msclassroom.shared.exception.NotFoundException;
import com.eduaircontrol.msclassroom.domain.model.Campus;
import com.eduaircontrol.msclassroom.domain.model.RecordStatus;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CampusServiceTest {

    private CampusRepository campusRepository;
    private CampusService campusService;

    @BeforeEach
    void setUp() {
        campusRepository = mock(CampusRepository.class);
        campusService = new CampusService(campusRepository);
    }

    private Campus campus(UUID id) {
        return Campus.builder()
                .id(id)
                .code("CAMP-1")
                .name("Campus Uno")
                .status(RecordStatus.ACTIVE)
                .build();
    }

    @Test
    void getReturnsNotFoundForUnknownId() {
        UUID id = UUID.randomUUID();
        when(campusRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> campusService.get(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getReturnsNotFoundForSoftDeletedCampus() {
        UUID id = UUID.randomUUID();
        Campus deleted = campus(id);
        deleted.setDeletedAt(java.time.Instant.now());
        when(campusRepository.findById(id)).thenReturn(Optional.of(deleted));

        assertThatThrownBy(() -> campusService.get(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void createRejectsDuplicatedCode() {
        when(campusRepository.existsByCode("CAMP-1")).thenReturn(true);

        assertThatThrownBy(() -> campusService.create("camp-1", "Otro", null, null))
                .isInstanceOf(ConflictException.class);

        verify(campusRepository, never()).save(any());
    }

    @Test
    void createUppercasesCodeAndDefaultsToActive() {
        when(campusRepository.existsByCode("CAMP-9")).thenReturn(false);
        when(campusRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Campus created = campusService.create("camp-9", "Campus Nueve", "  Bogotá  ", null);

        assertThat(created.getCode()).isEqualTo("CAMP-9");
        assertThat(created.getStatus()).isEqualTo(RecordStatus.ACTIVE);
        assertThat(created.getCity()).isEqualTo("Bogotá");
        assertThat(created.getDeletedAt()).isNull();
    }

    @Test
    void deleteAppliesSoftDelete() {
        UUID id = UUID.randomUUID();
        Campus existing = campus(id);
        when(campusRepository.findById(id)).thenReturn(Optional.of(existing));
        when(campusRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        campusService.delete(id);

        assertThat(existing.getDeletedAt()).isNotNull();
        assertThat(existing.getStatus()).isEqualTo(RecordStatus.ACTIVE);
    }
}
