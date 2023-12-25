
package MyProjects.Accede.controllers;

import java.util.HashSet;
import java.util.Set;

import MyProjects.Accede.dto.role.UserRoleRequestDTO;
import MyProjects.Accede.entities.Role;
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
    public void setUserRoles(@RequestParam int userId, String userRoles) {
        this.userService.setRoles(userId, userRoles);
    }

    @PutMapping("updateDescr")
    public void updateDescr(@RequestParam Integer userId, String profDescr){
        this.userService.updateDescr(userId, profDescr);
    }

    @GetMapping("checkRoles")
    public ResponseEntity<Set<String>> checkRoles(@RequestParam String username){
        return new ResponseEntity<>(this.userService.checkRoles(username), HttpStatus.OK);
    }

    @PutMapping("addRep")
    public void addRep(@RequestParam String username){
        this.userService.addRep(username);
    }
}
