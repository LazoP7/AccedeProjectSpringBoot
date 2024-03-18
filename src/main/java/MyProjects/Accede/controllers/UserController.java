
package MyProjects.Accede.controllers;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import MyProjects.Accede.dto.user.UserDTO;

import MyProjects.Accede.services.UserService;

@CrossOrigin
@RestController
@RequestMapping({"user"})
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping({"createUser"})//mapping for creating a new User. Object UserDTO
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        this.userService.createUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping({"{id}"})//mapping for getting user based on user id. Returns userDTO object
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO userDTO = this.userService.getUserById(id);
        if(userDTO == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping({"search-by-username"}) //mapping for getting user based on username. Returns userDTO object
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        UserDTO userDTO = this.userService.getUserByUsername(username);
        if(userDTO == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @PutMapping({"roles"}) //mapping for setting user roles
    public void setUserRoles(@RequestParam int userId, String userRoles) {
        this.userService.setRoles(userId, userRoles);
    }

    @PutMapping("updateDescr") //mapping for updating user profile description
    public void updateDescr(@RequestParam Integer userId, String profDescr){
        this.userService.updateDescr(userId, profDescr);
    }

    @GetMapping("checkRoles") //mapping for checking user roles
    public ResponseEntity<Set<String>> checkRoles(@RequestParam String username){
        return new ResponseEntity<>(this.userService.checkRoles(username), HttpStatus.OK);
    }

    @PutMapping("addRep") //mapping for adding reputation points to user
    public void addRep(@RequestParam String username){
        this.userService.addRep(username);
    }
}
