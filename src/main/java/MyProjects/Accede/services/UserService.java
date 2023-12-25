
package MyProjects.Accede.services;

import jakarta.transaction.Transactional;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
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
        logger.info(user.getMail());
    }

    public UserDTO getUserById(Integer id) {
        User user = (User)this.userRepository.getReferenceById(id);
        return this.userMapper.UsertoUserDTO(user);
    }

    @Transactional
    public void setRoles(int userId, String strRoles) {
        String[] listroles = new String[5];
        listroles = strRoles.split(",");
        Set<String> roles = new HashSet<>(Arrays.asList(listroles));
        Set<Role> roleSet = new HashSet<>();
        Integer roleId = 3;
        for(String strRole: roles){
            if(strRole.equals("Player")){
                roleId = 3;
            }
            if(strRole.equals("Owner")){
                roleId = 2;
            }
            if(strRole.equals("Admin")){
                roleId = 1;
            }
            roleSet.add(this.roleRepository.getReferenceById(roleId));
        }
        User user = this.userRepository.getReferenceById(userId);
        user.setRoles(roleSet);
        this.userRepository.save(user);
    }

    public UserDTO getUserByUsername(String username) {
        UserDTO user = this.userMapper.UsertoUserDTO(this.userRepository.findByUsername(username));
        logger.info(user.getId() + "");
        return user;
    }

    public void updateDescr(Integer userId, String profDescr) {
        userRepository.updateDescr(userId, profDescr);
    }

    public Set<String> checkRoles(String username) {
        User user = userRepository.findByUsername(username);
        Set<Role> roles = user.getRoles();
        Set<String> strRoles = new HashSet<>();
        for(Role role : roles){
            if(role.getRoleName().equals("ROLE_USER")){
                strRoles.add("Player");
            }
            if(role.getRoleName().equals("ROLE_OWNER")){
                strRoles.add("Owner");
            }
            if(role.getRoleName().equals("ROLE_ADMIN")){
                strRoles.add("Admin");
            }
        }
        return strRoles;
    }
    @Transactional
    public void addRep(String username) {
        User user = userRepository.findByUsername(username);
        user.setReputation(user.getReputation()+1);
        userRepository.save(user);
    }
}
