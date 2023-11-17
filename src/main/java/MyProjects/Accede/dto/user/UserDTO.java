
package MyProjects.Accede.dto.user;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String surname;
    private String username;
    private String mail;
    private String password;
    private Integer reputation;
    private Integer age;
    private String profDescr;

}