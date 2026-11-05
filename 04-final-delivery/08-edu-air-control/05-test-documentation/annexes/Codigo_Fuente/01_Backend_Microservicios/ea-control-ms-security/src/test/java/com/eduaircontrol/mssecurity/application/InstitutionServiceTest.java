package com.eduaircontrol.mssecurity.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eduaircontrol.mssecurity.application.port.InstitutionRepository;
import com.eduaircontrol.mssecurity.domain.exception.ConflictException;
import com.eduaircontrol.mssecurity.domain.exception.NotFoundException;
import com.eduaircontrol.mssecurity.domain.exception.ValidationException;
import com.eduaircontrol.mssecurity.domain.model.Institution;
import com.eduaircontrol.mssecurity.domain.model.InstitutionStatus;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InstitutionServiceTest {

    private InstitutionRepository repository;
    private InstitutionService service;

    @BeforeEach
    void setUp() {
        repository = mock(InstitutionRepository.class);
        service = new InstitutionService(repository);
    }

    private Institution institution(UUID id, String code) {
        return Institution.builder().id(id).code(code).name("SENA")
                .status(InstitutionStatus.ACTIVE).build();
    }

    @Test
    void createUppercasesCodeAndDefaultsActive() {
        when(repository.existsByCode("SEN-4444")).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Institution created = service.create("sen-4444", "SENA", "  education  ");

        assertThat(created.getCode()).isEqualTo("SEN-4444");
        assertThat(created.getStatus()).isEqualTo(InstitutionStatus.ACTIVE);
        assertThat(created.getType()).isEqualTo("education");
    }

    @Test
    void createRejectsDuplicatedCode() {
        when(repository.existsByCode("SEN-4444")).thenReturn(true);

        assertThatThrownBy(() -> service.create("SEN-4444", "SENA", null))
                .isInstanceOf(ConflictException.class)
                .hasFieldOrPropertyWithValue("code", "INSTITUTION_CODE_EXISTS");
        verify(repository, never()).save(any());
    }

    @Test
    void createRejectsBlankName() {
        when(repository.existsByCode("SEN-4444")).thenReturn(false);

        assertThatThrownBy(() -> service.create("SEN-4444", "  ", null))
                .isInstanceOf(ValidationException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void getReturnsNotFoundForUnknownId() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(id)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateChangesProvidedFields() {
        UUID id = UUID.randomUUID();
        Institution existing = institution(id, "SEN-4444");
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Institution updated = service.update(id, "SENA Regional", "education",
                InstitutionStatus.INACTIVE);

        assertThat(updated.getName()).isEqualTo("SENA Regional");
        assertThat(updated.getStatus()).isEqualTo(InstitutionStatus.INACTIVE);
    }
}
