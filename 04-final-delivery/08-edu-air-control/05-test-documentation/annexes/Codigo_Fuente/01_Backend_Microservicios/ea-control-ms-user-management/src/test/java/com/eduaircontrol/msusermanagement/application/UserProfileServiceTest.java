package com.eduaircontrol.msusermanagement.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eduaircontrol.msusermanagement.domain.port.out.UserProfileRepository;
import com.eduaircontrol.msusermanagement.shared.exception.ConflictException;
import com.eduaircontrol.msusermanagement.shared.exception.NotFoundException;
import com.eduaircontrol.msusermanagement.shared.exception.ValidationException;
import com.eduaircontrol.msusermanagement.domain.model.RecordStatus;
import com.eduaircontrol.msusermanagement.domain.model.UserProfile;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserProfileServiceTest {

    private UserProfileRepository repository;
    private UserProfileService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserProfileRepository.class);
        service = new UserProfileService(repository);
    }

    private UserProfile profile(UUID id, UUID userId) {
        return UserProfile.builder()
                .id(id)
                .userId(userId)
                .fullName("Ada Lovelace")
                .status(RecordStatus.ACTIVE)
                .build();
    }

    @Test
    void getReturnsNotFoundForUnknownId() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(id)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getReturnsNotFoundForSoftDeletedProfile() {
        UUID id = UUID.randomUUID();
        UserProfile deleted = profile(id, UUID.randomUUID());
        deleted.setDeletedAt(Instant.now());
        when(repository.findById(id)).thenReturn(Optional.of(deleted));

        assertThatThrownBy(() -> service.get(id)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getByUserIdReturnsNotFoundWhenAbsent() {
        UUID userId = UUID.randomUUID();
        when(repository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByUserId(userId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void createRejectsDuplicatedUserId() {
        UUID userId = UUID.randomUUID();
        when(repository.existsByUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> service.create(userId, "Ada", null, null, null, null, null))
                .isInstanceOf(ConflictException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void createTrimsTextAndDefaultsToActive() {
        UUID userId = UUID.randomUUID();
        when(repository.existsByUserId(userId)).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile created = service.create(userId, "  Ada Lovelace  ", " 3001234567 ",
                "  Systems  ", null, null, "  https://img.example/ada.png  ");

        assertThat(created.getFullName()).isEqualTo("Ada Lovelace");
        assertThat(created.getPhone()).isEqualTo("3001234567");
        assertThat(created.getDepartment()).isEqualTo("Systems");
        assertThat(created.getAvatarUrl()).isEqualTo("https://img.example/ada.png");
        assertThat(created.getStatus()).isEqualTo(RecordStatus.ACTIVE);
        assertThat(created.getDeletedAt()).isNull();
    }

    @Test
    void createRejectsBlankFullName() {
        UUID userId = UUID.randomUUID();
        when(repository.existsByUserId(userId)).thenReturn(false);

        assertThatThrownBy(() -> service.create(userId, "  ", null, null, null, null, null))
                .isInstanceOf(ValidationException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void updateChangesOnlyProvidedFields() {
        UUID id = UUID.randomUUID();
        UserProfile existing = profile(id, UUID.randomUUID());
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile updated = service.update(id, "Grace Hopper", null, null, null, null, null,
                RecordStatus.INACTIVE);

        assertThat(updated.getFullName()).isEqualTo("Grace Hopper");
        assertThat(updated.getStatus()).isEqualTo(RecordStatus.INACTIVE);
    }

    @Test
    void deleteAppliesSoftDelete() {
        UUID id = UUID.randomUUID();
        UserProfile existing = profile(id, UUID.randomUUID());
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.delete(id);

        assertThat(existing.getDeletedAt()).isNotNull();
    }
}
