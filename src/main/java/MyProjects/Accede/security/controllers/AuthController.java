
package MyProjects.Accede.security.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import MyProjects.Accede.security.dto.JwtDTO;
import MyProjects.Accede.security.dto.UserLogin;
import MyProjects.Accede.security.dto.UserLogin2Fact;
import MyProjects.Accede.security.jwt.JwtUtils;
import MyProjects.Accede.security.ott.OTTUtility;
import MyProjects.Accede.services.EmailService;

@RestController
@CrossOrigin
@RequestMapping({"auth"})
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    OTTUtility ottUtility;
    @Autowired
    EmailService emailService;

    @PostMapping("/signin") //mapping for sign in
    public ResponseEntity<JwtDTO> authenticateUser(@RequestBody UserLogin userLogin) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword());

        try {
            Authentication auth = this.authenticationManager.authenticate(authentication);
            JwtDTO token = this.jwtUtils.generateJwtToken(auth, userLogin.getRememberMe());
            log.info("Generisani token je : {}", token.getAccessToken());
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } catch (Exception var5) {
            log.error("Expection triggered: {}", var5.getMessage());
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping({"signin_2fact"}) //mapping for 2-factor authentication sign in
    public ResponseEntity<Void> authenticateUser2Fact(@RequestBody UserLogin userLogin) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword());

        try {
            Authentication auth = this.authenticationManager.authenticate(authentication);
            Integer ott = this.ottUtility.generateOtt(auth.getName());
            log.info("Generisani ott je : {}", ott);
            this.emailService.sendMessage(userLogin.getUsername(), "OTT code", ott.toString());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception var5) {
            log.error("Exception triggered: {}", var5.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping({"signin_ott"}) //mapping for authenticating user based on OTT. Requests object of username and OTT
    public ResponseEntity<JwtDTO> authenticateUserWithOTT(@RequestBody UserLogin2Fact userLogin2Fact) {
        if (userLogin2Fact.getOtt().equals(this.ottUtility.getOTTByKey(userLogin2Fact.getUsername()))) {
            JwtDTO token = this.jwtUtils.generateJwtTokenWith2Fact(userLogin2Fact.getUsername());
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}

