package Shopping.BackendCart.dto;

/**
 * User DTO - Data Transfer Object for User
 * Contains only the necessary fields for business logic (no IDs for creation)
 */
public class UserDTO {

    private Integer personId;
    private String email;
    private String password;
    private Boolean isActive;

    // Constructors
    public UserDTO() {}

    public UserDTO(Integer personId, String email, String password, Boolean isActive) {
        this.personId = personId;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

    // Getters and setters
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
