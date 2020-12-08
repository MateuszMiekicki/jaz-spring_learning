package pl.edu.pjwstk.jaz.task.third.user;

public class UserDTO {
    private String username = null;
    private String password = null;
    private String idRole = null;

    public UserDTO() {
    }

    public UserDTO(String username, String password, String idRole) {
        this.username = username;
        this.password = password;
        this.idRole = idRole;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIdRole() {
        return idRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public boolean isEmpty() {
        return (username.isEmpty() && password.isEmpty() && idRole.isEmpty());
    }

    public boolean usernameIsEmpty() {
        return username.isEmpty();
    }

    public boolean passwordIsEmpty() {
        return password.isEmpty();
    }

    public boolean idRoleIsEmpty() {
        return idRole.isEmpty();
    }
}
