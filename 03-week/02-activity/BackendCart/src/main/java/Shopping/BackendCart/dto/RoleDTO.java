package Shopping.BackendCart.dto;

/**
 * Role DTO - Data Transfer Object for Role
 * Contains only the necessary fields for business logic (no IDs for creation)
 */
public class RoleDTO {

    private String name;
    private String description;
    private Boolean isActive;

    // Constructors
    public RoleDTO() {}

    public RoleDTO(String name, String description, Boolean isActive) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
