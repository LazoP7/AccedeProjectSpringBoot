
package MyProjects.Accede.services;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import MyProjects.Accede.dto.role.RoleDTO;
import MyProjects.Accede.dto.user.UserDTO;
import MyProjects.Accede.entities.Role;
import MyProjects.Accede.entities.User;
import MyProjects.Accede.map.UserMapper;
import MyProjects.Accede.repositories.RoleRepository;
import MyProjects.Accede.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    public UserService() {
    }

    public void createUser(UserDTO userDTO) {
        userDTO.setProfDescr("");
        userDTO.setReputation(0);
        User user = this.userMapper.UserDTOtoUser(userDTO);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPW = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPW);
        Set<Role> roles = new HashSet();
        Role role = roleRepository.getReferenceById(3);
        roles.add(role);
        user.setRoles(roles);
        this.userRepository.save(user);
        logger.info(user.getUsername());
    }

    public UserDTO getUserById(Integer id) {
        User user = (User)this.userRepository.getReferenceById(id);
        return this.userMapper.UsertoUserDTO(user);
    }

    @Transactional
    public void setRoles(int userId, Set<RoleDTO> rolesDTO) {
        Set<Role> roles2 = new HashSet();
        Iterator var4 = rolesDTO.iterator();

        while(var4.hasNext()) {
            RoleDTO role = (RoleDTO)var4.next();
            logger.info(Integer.toString(role.getId()));
            roles2.add((Role)this.roleRepository.getReferenceById(role.getId()));
        }

        User user = (User)this.userRepository.getReferenceById(userId);
        user.setRoles(roles2);
        this.userRepository.save(user);
    }

    public UserDTO getUserByUsername(String username) {
        return this.userMapper.UsertoUserDTO(this.userRepository.findByUsername(username));
    }
}
