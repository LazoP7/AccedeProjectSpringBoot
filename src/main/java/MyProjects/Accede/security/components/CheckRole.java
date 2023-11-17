
package MyProjects.Accede.security.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import MyProjects.Accede.entities.User;
import MyProjects.Accede.repositories.UserRepository;

@Component
public class CheckRole {
    @Autowired
    UserRepository userRepository;

    public CheckRole() {
    }

    public boolean isAdmin(Integer id) {
        User user = (User)this.userRepository.getReferenceById(id);
        return user.getRoles().contains("ROLE_ADMIN");
    }

    public boolean isOwner(Integer id) {
        User user = (User)this.userRepository.getReferenceById(id);
        return user.getRoles().contains("ROLE_OWNER");
    }

    public boolean isPlayer(Integer id) {
        User user = (User)this.userRepository.getReferenceById(id);
        return user.getRoles().contains("ROLE_PLAYER");
    }
}
