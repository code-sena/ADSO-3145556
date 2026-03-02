package Shopping.BackendCart.service;

import Shopping.BackendCart.dto.RoleDTO;
import Shopping.BackendCart.entity.Role;
import Shopping.BackendCart.repository.IRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Role Service Implementation - implements IRoleService
 */
@Service
@Transactional
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDTO> findById(Integer id) {
        return roleRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDTO> findByName(String name) {
        return roleRepository.findByName(name)
                .map(this::mapToDTO);
    }

    @Override
    public RoleDTO save(RoleDTO dto) {
        Role role = mapToEntity(dto);
        Role savedRole = roleRepository.save(role);
        return mapToDTO(savedRole);
    }

    @Override
    public RoleDTO update(Integer id, RoleDTO dto) {
        return roleRepository.findById(id)
                .map(existingRole -> {
                    existingRole.setName(dto.getName());
                    existingRole.setDescription(dto.getDescription());
                    existingRole.setIsActive(dto.getIsActive());
                    Role updatedRole = roleRepository.save(existingRole);
                    return mapToDTO(updatedRole);
                })
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Override
    public void deleteById(Integer id) {
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    // Mappers
    private Role mapToEntity(RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return role;
    }

    private RoleDTO mapToDTO(Role role) {
        return new RoleDTO(
                role.getName(),
                role.getDescription(),
                role.getIsActive()
        );
    }
}
