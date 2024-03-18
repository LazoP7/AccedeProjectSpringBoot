
package MyProjects.Accede.security.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import MyProjects.Accede.entities.User;
import MyProjects.Accede.repositories.UserRepository;

@Component
public class CheckRole {
    @Autowired
    UserRepository userRepository;

    public boolean isAdmin(Integer id) //simple method for checking if user role is Admin
    {
        User user = this.userRepository.getReferenceById(id);
        return user.getRoles().contains("ROLE_ADMIN");
    }

    public boolean isOwner(Integer id) //simple method for checking if user role is Owner
    {
        User user = this.userRepository.getReferenceById(id);
        return user.getRoles().contains("ROLE_OWNER");
    }

    public boolean isPlayer(Integer id) //simple method for checking if user role is Player
    {
        User user = this.userRepository.getReferenceById(id);
        return user.getRoles().contains("ROLE_PLAYER");
    }
}
