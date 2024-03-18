
package MyProjects.Accede.services;

import jakarta.transaction.Transactional;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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

    public void createUser(UserDTO userDTO) //service for creating user
    {
        userDTO.setProfDescr(""); //setting user profile description to empty
        userDTO.setReputation(0); //setting user reputation to 0
        User user = this.userMapper.UserDTOtoUser(userDTO); //mapping userDTO to user
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPW = bCryptPasswordEncoder.encode(user.getPassword()); //password encryption
        user.setPassword(encodedPW); //setting users encrypted password into database
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.getReferenceById(3);
        roles.add(role);
        user.setRoles(roles); //adding roles to user
        this.userRepository.save(user);
    }

    public UserDTO getUserById(Integer id) //service for getting user based on id
    {
        User user = (User)this.userRepository.getReferenceById(id);
        return this.userMapper.UsertoUserDTO(user);
    }

    @Transactional
    public void setRoles(int userId, String strRoles) //service for setting roles of user
    {
        String[] listroles = strRoles.split(",");
        Set<String> roles = new HashSet<>(Arrays.asList(listroles));
        Set<Role> roleSet = new HashSet<>();
        int roleId = 3;
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

    public UserDTO getUserByUsername(String username) //service for returning user based on username
    {
        return this.userMapper.UsertoUserDTO(this.userRepository.findByUsername(username));
    }

    public void updateDescr(Integer userId, String profDescr) //service for updating user profile description
    {
        userRepository.updateDescr(userId, profDescr);
    }

    public Set<String> checkRoles(String username) //service for checking user roles
    {
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
    public void addRep(String username) //service for adding reputation
    {
        User user = userRepository.findByUsername(username);
        user.setReputation(user.getReputation()+1);
        userRepository.save(user);
    }
}
