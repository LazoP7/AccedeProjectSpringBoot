
package MyProjects.Accede.services;


import jakarta.transaction.Transactional;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MyProjects.Accede.dto.match.MatchDTO;
import MyProjects.Accede.entities.Location;
import MyProjects.Accede.entities.SportMatch;
import MyProjects.Accede.entities.User;
import MyProjects.Accede.map.MatchMapper;
import MyProjects.Accede.map.UserMapper;
import MyProjects.Accede.repositories.LocationRepository;
import MyProjects.Accede.repositories.MatchRepository;
import MyProjects.Accede.repositories.UserRepository;

@Service
public class MatchService {
    @Autowired
    MatchRepository matchRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MatchMapper matchMapper;

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    public ArrayList<MatchDTO> getMatchesByLocation(String location, int type) //service for getting matches based on location
//   list of matches is mapped to a list of matchesDTO objects
    {
        ArrayList<SportMatch> sportMatches = matchRepository.findByLocation(location, type);
        ArrayList<MatchDTO> matchesDTO = matchMapper.SportMatchestoMatchesDTO(sportMatches);
        matchesDTO.sort(Comparator.comparing(MatchDTO::getDate));
        return matchesDTO;
    }

    @Transactional
    public void syncMatches() //service for deleting matches before current date and creating new one 7 days later
    {
        ArrayList<SportMatch> sportMatches = matchRepository.getAllMatches();
        Calendar today = Calendar.getInstance();

        for(SportMatch sportMatch : sportMatches) {
            if (sportMatch.getDate().get(Calendar.DAY_OF_YEAR) < today.get(Calendar.DAY_OF_YEAR)) {
                this.matchRepository.deleteByDate(sportMatch.getDate(),0);
                this.matchRepository.deleteByDate(sportMatch.getDate(),1);
                this.matchRepository.deleteByDate(sportMatch.getDate(),2);
                sportMatch.setPlayers(null);
                sportMatch.getDate().set(Calendar.DATE, sportMatch.getDate().get(Calendar.DATE) + 8);
            }
        }
    }

    @Transactional
    public void insertMatches(String locationName, int type) //service for creating new matches for provided location
    {
        boolean hasMatches = false;
        Location location = locationRepository.getLocationByName(locationName);
        List<SportMatch> sportMatches = location.getSportMatches();
        for(SportMatch match : sportMatches){
            if(match.getType() == type){
                hasMatches = true;
            }
        }
        Calendar calKeeper = Calendar.getInstance();
        if(sportMatches.isEmpty() || !hasMatches) {
            for (int j = 0; j <= 8; ++j) {
                for (int i = 7; i <= 22; ++i) {
                    Calendar rightNow = Calendar.getInstance();
                    rightNow.set(Calendar.MINUTE, 0);
                    rightNow.set(Calendar.SECOND, 0);
                    rightNow.set(Calendar.MILLISECOND, 0);
                    rightNow.set(Calendar.DATE, calKeeper.get(Calendar.DATE));
                    rightNow.set(Calendar.YEAR, calKeeper.get(Calendar.YEAR));
                    SportMatch sportMatch = new SportMatch();
                    sportMatch.setNum_of_players(4);
                    sportMatch.setLocation(location);
                    sportMatch.setType(type);
                    rightNow.set(Calendar.HOUR_OF_DAY, i);
                    sportMatch.setDate(rightNow);
                    sportMatch.setOpen(true);
                    sportMatches.add(sportMatch);
                }

                calKeeper.add(Calendar.DATE, 1);
                logger.info(calKeeper.get(Calendar.DATE) + ":" + calKeeper.get(Calendar.YEAR));
            }

            location.setSportMatches(sportMatches);
            locationRepository.updateLocation(location, location.getId());
        }
    }

    public ArrayList<MatchDTO> getMatchesByLocationAndDate(String locationName,int year, int month, int day, int type)
            //service for getting all matches based on provided location and date
    {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1);
        date.set(Calendar.DATE, day);
        ArrayList<SportMatch> sportMatches = matchRepository.getByLocationAndDay(locationName, date, type);
        return matchMapper.SportMatchestoMatchesDTO(sportMatches);
    }

    public ArrayList<MatchDTO> getMatchesByDate(int year, int month, int day, int type)
        //service for getting all matches based on provided date. Returns a list of matchDTO
    {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1); //month is decremented by 1. MYSQL uses 0-11 for its months. While angular uses 1-12 for its months
        date.set(Calendar.DATE, day);
        ArrayList<SportMatch> sportMatches = matchRepository.getMatchByDate(date, type);
        return matchMapper.SportMatchestoMatchesDTO(sportMatches);
    }

    public MatchDTO getMatchesByDateAndTime(String locationName, String stringDate, int type)
            //service for getting all matches based on provided date and time
    {
        String[] splitDateTime = stringDate.split("T");
        String[] time = splitDateTime[1].split(":");
        int hour = Integer.parseInt(time[0]) + 1;
        String formattedHour = String.format("%02d", hour); // Ensure two-digit representation
        String dateTime = splitDateTime[0] + " " + formattedHour + ":00:00"; // Setting seconds as 00
        SportMatch sportMatch = matchRepository.getMatchByDateAndTime(locationName, dateTime, type);
        return this.matchMapper.SportMatchtoMatchDTO(sportMatch);
    }

    @Transactional
    public void setPlayer(int userId, String locationName, String stringDate, int type) //service for setting players to chosen matches
    {
        String[] splitDateTime = stringDate.split("T");
        String[] time = splitDateTime[1].split(":");
        int hour = Integer.parseInt(time[0]) + 1;
        String formattedHour = String.format("%02d", hour); // Ensure two-digit representation
        String dateTime = splitDateTime[0] + " " + formattedHour + ":00:00"; // Setting seconds as 00
        SportMatch match = matchRepository.getMatchByDateAndTime(locationName, dateTime, type);
        User user = userRepository.getReferenceById(userId);
        match.addPlayer(user);
    }


    public ArrayList<MatchDTO> getAllMatches() //service for getting all matches
    {
        ArrayList<SportMatch> sportMatches = matchRepository.getAllMatches();
        return matchMapper.SportMatchestoMatchesDTO(sportMatches);
    }

    public ArrayList<MatchDTO> myMatches(String username) //service for getting matches based on provided player(user) username
    {
        ArrayList<SportMatch> sportMatches = matchRepository.getMyMatches(username);
        return matchMapper.SportMatchestoMatchesDTO(sportMatches);
    }


    public void changeMatchStatus(String locationName, String stringDate, int type)
        //service for setting match status. Open or Closed
    {
        String[] splitDateTime = stringDate.split("T"); // Splitting provided date string into date and time
        String[] time = splitDateTime[1].split(":"); // Splitting time string into string for hours, minutes, seconds, etc.
        int hour = Integer.parseInt(time[0]) + 1; //parsing hour string into integer
        String formattedHour = String.format("%02d", hour); // Ensure two-digit representation
        String dateTime = splitDateTime[0] + " " + formattedHour + ":00:00"; // Setting seconds as 00
        SportMatch match = matchRepository.getMatchByDateAndTime(locationName, dateTime, type);
        match.setOpen(!match.isOpen());
        matchRepository.save(match);
    }

    public void kickPlayer(String locationName, String stringDate, String playerUsername, int type)
        //service for removing a player(user) from chosen match
    {
        String[] splitDateTime = stringDate.split("T");
        String[] time = splitDateTime[1].split(":");
        int hour = Integer.parseInt(time[0]) + 1;
        String formattedHour = String.format("%02d", hour);
        String dateTime = splitDateTime[0] + " " + formattedHour + ":00:00";
        SportMatch match = matchRepository.getMatchByDateAndTime(locationName, dateTime, type);
        Set<User> players = match.getPlayers();
        User user = userRepository.findByUsername(playerUsername);
        players.remove(user);
        match.setPlayers(players);
        matchRepository.save(match);
    }

    public void setNumOfPlayers(int size, int matchId) //service for changing number of allowed players in a match
    {
        SportMatch match = matchRepository.getById(matchId);
        match.setNum_of_players(size);
        matchRepository.save(match);
    }

    public ArrayList<MatchDTO> matchesByType(int type) //service for getting all matches based on match type
    {
        ArrayList<SportMatch> sportMatches = matchRepository.getMatchesByType(type);
        return matchMapper.SportMatchestoMatchesDTO(sportMatches);
    }
}
