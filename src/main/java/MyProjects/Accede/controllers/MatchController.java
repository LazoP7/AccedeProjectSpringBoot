
package MyProjects.Accede.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping("/sync") //mapping for syncing matches to current date
        // Automatically deletes matches with past dates and creates corresponding new ones
    void syncMatches() {
        this.matchService.syncMatches();
    }

    @PutMapping("/populate") //mapping for creating and binding new matches to new location based on current date
    void populateLocation(@RequestParam String locationName, int type) {
        this.matchService.insertMatches(locationName, type);
    }

    @GetMapping("/allMatches")//mapping for getting all matches. Returns a list of matchDTO objects
    ResponseEntity<ArrayList<MatchDTO>> getAllMatches() {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getAllMatches();
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping("/location")//mapping for getting all matches based on provided location name. Returns a list of matchDTO objects
    ResponseEntity<ArrayList<MatchDTO>> getMatchesByLocation(@RequestParam String locationName, int type) {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getMatchesByLocation(locationName, type);
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping("/locationANDdate")//mapping for getting all matches based on provided location name and date. Returns a list of matchDTO objects
    ResponseEntity<ArrayList<MatchDTO>> getMatchesByLocationAndDate(@RequestParam String locationName,int year, int month, int day, int type) {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getMatchesByLocationAndDate(locationName,year, month, day, type);
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping("/date")//mapping for getting all matches based on provided date. Returns a list of matchDTO objects
    ResponseEntity<ArrayList<MatchDTO>> getMatchesByDate(@RequestParam int year, int month, int day, int type) {
        ArrayList<MatchDTO> matchesDTO = this.matchService.getMatchesByDate(year, month, day, type);
        if (matchesDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchesDTO, HttpStatus.OK);
    }

    @GetMapping("/time")//mapping for getting a match based on provided time. Returns a matchDTO object
    ResponseEntity<MatchDTO> getMatchesByTime(@RequestParam String locationName, String stringDate, int type) {
        MatchDTO matchDTO = this.matchService.getMatchesByDateAndTime(locationName, stringDate, type);
        if (matchDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchDTO, HttpStatus.OK);
    }

    @PutMapping("/player")//mapping for assigning player to a match
    public void setPlayer(@RequestParam int userId, String locationName, String date, int type) {
        this.matchService.setPlayer(userId, locationName, date, type);
    }

    @GetMapping("/myMatches")//mapping for getting all matches with provided player. Returns a matchDTO object
    public ArrayList<MatchDTO> myMatches(@RequestParam String username) {
        return this.matchService.myMatches(username);
    }

    @PutMapping("/changeMatchStatus")//mapping for changing match status. Matches can be Open or Closed
    public void changeMatchStatus(@RequestParam String locationName, String date, int type){
        matchService.changeMatchStatus(locationName, date, type);
    }

    @PutMapping("/kickPlayer")//mapping for kicking players from a match
    public void kickPlayer(@RequestParam String locationName, String date, String playerUsername, int type){
        matchService.kickPlayer(locationName, date, playerUsername, type);
    }

    @PutMapping("/setNumOfPlayers")//mapping for changing number of allowed players in a match
    public void setNumOfPlayers(@RequestParam int size, int matchId){
        matchService.setNumOfPlayers(size, matchId);
    }

    @GetMapping("/getMatchesByType")//mapping for getting matches by type. Returns a list of matchDTO objects
    public ArrayList<MatchDTO> matchesByType (@RequestParam int type){
        return this.matchService.matchesByType(type);
    }
}
