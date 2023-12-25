
package MyProjects.Accede.controllers;

import java.util.ArrayList;

import MyProjects.Accede.entities.SportMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import MyProjects.Accede.dto.match.MatchDTO;
import MyProjects.Accede.services.MatchService;

@CrossOrigin
@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    MatchService matchService;

    @PutMapping("/sync")
    void syncMatches() {
        this.matchService.syncMatches();
    }

    @PutMapping("populate")
    void populateLocation(@RequestParam String locationName) {this.matchService.insertMatches(locationName);}

    @GetMapping("/allMatches")
    ResponseEntity<ArrayList<MatchDTO>> getAllMatches() {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getAllMatches();
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping("/location")
    ResponseEntity<ArrayList<MatchDTO>> getMatchesByLocation(@RequestParam String locationName) {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getMatchesByLocation(locationName);
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping("/locationANDdate")
    ResponseEntity<ArrayList<MatchDTO>> getMatchesByLocationAndDate(@RequestParam String locationName,int year, int month, int day) {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getMatchesByLocationAndDate(locationName,year, month, day);
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping("/date")
    ResponseEntity<ArrayList<MatchDTO>> getMatchesByDate(@RequestParam int year, int month, int day) {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getMatchesByDate(year, month, day);
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping("/time")
    ResponseEntity<MatchDTO> getMatchesByTime(@RequestParam String locationName, String stringDate) {
        MatchDTO matchDTO = this.matchService.getMatchesByDateAndTime(locationName, stringDate);
        if (matchDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchDTO, HttpStatus.OK);
    }

    @PutMapping("/player")
    public void setPlayer(@RequestParam int userId, String locationName, String date) {
        this.matchService.setPlayer(userId, locationName, date);
    }

    @GetMapping("/myMatches")
    public ArrayList<MatchDTO> myMatches(@RequestParam String username) {
        return this.matchService.myMatches(username);
    }

    @PutMapping("/changeMatchStatus")
    public void changeMatchStatus(@RequestParam String locationName, String date){
        matchService.changeMatchStatus(locationName, date);
    }

    @PutMapping("/kickPlayer")
    public void kickPlayer(@RequestParam String locationName, String date, String playerUsername){
        matchService.kickPlayer(locationName, date, playerUsername);
    }

    @PutMapping("/setNumOfPlayers")
    public void setNumOfPlayers(@RequestParam int size, int matchId){
        matchService.setNumOfPlayers(size, matchId);
    }
}
