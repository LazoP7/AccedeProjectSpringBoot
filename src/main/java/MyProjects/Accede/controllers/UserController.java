
package MyProjects.Accede.controllers;

import java.util.HashSet;
import java.util.Set;

import MyProjects.Accede.dto.role.UserRoleRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import MyProjects.Accede.dto.role.RoleDTO;
import MyProjects.Accede.dto.user.UserDTO;

import MyProjects.Accede.services.UserService;

@CrossOrigin
@RestController
@RequestMapping({"user"})
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping({"createUser"})
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        this.userService.createUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO userDTO = this.userService.getUserById(id);
        return userDTO == null ? new ResponseEntity(HttpStatus.NOT_FOUND) : new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @GetMapping({"search-by-username"})
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        UserDTO userDTO = this.userService.getUserByUsername(username);
        return userDTO == null ? new ResponseEntity(HttpStatus.NOT_FOUND) : new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @PutMapping({"roles"})
    public void setUserRoles(@RequestBody UserRoleRequestDTO requestDTO) {
        Set<RoleDTO> rolesDTO = new HashSet(requestDTO.getRoles());
        this.userService.setRoles(requestDTO.getUserId(), rolesDTO);
    }
}
