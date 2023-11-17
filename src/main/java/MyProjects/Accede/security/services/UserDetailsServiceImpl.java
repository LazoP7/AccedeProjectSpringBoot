
package MyProjects.Accede.security.services;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import MyProjects.Accede.dto.user.UserDTO;
import MyProjects.Accede.entities.Role;
import MyProjects.Accede.entities.User;
import MyProjects.Accede.map.UserMapper;
import MyProjects.Accede.services.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    public UserDetailsServiceImpl() {
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userTemp = this.userService.getUserByUsername(username);
        User userOptional = this.userMapper.UserDTOtoUser(userTemp);
        if (userOptional == null) {
            return null;
        } else {
            Set<Role> roles = userOptional.getRoles();
            List<GrantedAuthority> authorities = roles.stream().map((role) -> {
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
                return authority;
            }).toList();
            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(userOptional.getUsername(), userOptional.getPassword(), authorities);
            return user;
        }
    }
}