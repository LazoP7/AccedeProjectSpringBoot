
package MyProjects.Accede.controllers;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import MyProjects.Accede.dto.match.MatchDTO;
import MyProjects.Accede.services.MatchService;

@CrossOrigin
@RestController
@RequestMapping({"match"})
public class MatchController {
    @Autowired
    MatchService matchService;

    @PutMapping({"sync"})
    void syncMatches() {
        this.matchService.syncMatches();
    }

    @GetMapping({"allMatches"})
    ResponseEntity<ArrayList<MatchDTO>> getAllMatches() {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getAllMatches();
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping({"location"})
    ResponseEntity<ArrayList<MatchDTO>> getMatchesByLocation(@RequestParam String locationName) {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getMatchesByLocation(locationName);
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @PostMapping
    void newMatches(@RequestParam String locationName) {
        matchService.insertMatches(locationName);
    }

    @GetMapping({"locationANDdate"})
    ResponseEntity<ArrayList<MatchDTO>> getMatchesByLocationAndDate(@RequestParam String locationName, int month, int day) {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getMatchesByLocationAndDate(locationName, month, day);
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping({"date"})
    ResponseEntity<ArrayList<MatchDTO>> getMatchesByDate(@RequestParam int year, int month, int day) {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getMatchesByDate(year, month, day);
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping({"time"})
    ResponseEntity<MatchDTO> getMatchesByTime(@RequestParam String locationName, String stringDate) {
        MatchDTO matchDTO = this.matchService.getMatchesByDateAndTime(locationName, stringDate);
        if (matchDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchDTO, HttpStatus.OK);
    }

    @PutMapping({"player"})
    public void setPlayer(@RequestParam int userId, String locationName, String date) {
        this.matchService.setPlayer(userId, locationName, date);
    }

    @GetMapping({"myMatches"})
    public ArrayList<MatchDTO> myMatches(@RequestParam String username) {
        return this.matchService.myMatches(username);
    }
}
