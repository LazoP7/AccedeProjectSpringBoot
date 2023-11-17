
package MyProjects.Accede.security.dto;

import lombok.Data;

@Data
public class UserLogin {
    private String username;
    private String password;
    private boolean rememberMe;

    public boolean getRememberMe() {
        return this.rememberMe;
    }
}
