
package MyProjects.Accede.services;


import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MyProjects.Accede.dto.match.MatchDTO;
import MyProjects.Accede.dto.user.UserDTO;
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
    @Autowired
    UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    public ArrayList<MatchDTO> getMatchesByLocation(String location) {
        ArrayList<SportMatch> sportMatches = matchRepository.findByLocation(location);
        ArrayList<MatchDTO> matchesDTO = matchMapper.SportMatchestoMatchesDTO(sportMatches);
        matchesDTO.sort(Comparator.comparing(MatchDTO::getDate));
        return matchesDTO;
    }

    @Transactional
    public void syncMatches() {
        ArrayList<SportMatch> sportMatches = matchRepository.getAllMatches();
        Calendar today = Calendar.getInstance();

        for(SportMatch sportMatch : sportMatches) {
            if (sportMatch.getDate().get(Calendar.DAY_OF_YEAR) < today.get(Calendar.DAY_OF_YEAR)) {
                this.matchRepository.deleteByDate(sportMatch.getDate());
                sportMatch.setPlayers(null);
                sportMatch.getDate().set(Calendar.DATE, sportMatch.getDate().get(Calendar.DATE) + 8);
            }
        }



    }

    @Transactional
    public void insertMatches(String locationName) {
        Location location = locationRepository.getLocationByName(locationName);
        List<SportMatch> sportMatches = location.getSportMatches();
        Calendar calKeeper = Calendar.getInstance();

        for(int j = 0; j <= 8; ++j) {
            for(int i = 7; i <= 22; ++i) {
                Calendar rightNow = Calendar.getInstance();
                rightNow.set(Calendar.MINUTE, 0);
                rightNow.set(Calendar.SECOND, 0);
                rightNow.set(Calendar.MILLISECOND, 0);
                rightNow.set(Calendar.DATE, calKeeper.get(Calendar.DATE));
                SportMatch sportMatch = new SportMatch();
                sportMatch.setNum_of_players(4);
                sportMatch.setLocation(location);
                rightNow.set(Calendar.HOUR_OF_DAY, i);
                sportMatch.setDate(rightNow);
                sportMatches.add(sportMatch);
            }

            calKeeper.add(Calendar.DATE, 1);
        }

        location.setSportMatches(sportMatches);
        locationRepository.updateLocation(location, location.getId());
    }

    public ArrayList<MatchDTO> getMatchesByLocationAndDate(String locationName, int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MONTH, month - 1);
        date.set(Calendar.DATE, day);
        ArrayList<SportMatch> sportMatches = matchRepository.getByLocationAndDay(locationName, date);
        return matchMapper.SportMatchestoMatchesDTO(sportMatches);
    }

    public ArrayList<MatchDTO> getMatchesByDate(int year, int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1);
        date.set(Calendar.DATE, day);
        ArrayList<SportMatch> sportMatches = matchRepository.getMatchByDate(date);
        return matchMapper.SportMatchestoMatchesDTO(sportMatches);
    }

    public MatchDTO getMatchesByDateAndTime(String locationName, String stringDate) {
        SportMatch sportMatch = matchRepository.getMatchByDateAndTime(locationName, stringDate);
        return this.matchMapper.SportMatchtoMatchDTO(sportMatch);
    }

    @Transactional
    public void setPlayer(int userId, String locationName, String stringDate) {
        SportMatch match = matchRepository.getMatchByDateAndTime(locationName, stringDate);
        User user = userRepository.getReferenceById(userId);
        Set<User> players = match.getPlayers();
        Set<SportMatch> userMatches = userRepository.getMatchesForUser(userId);
        players.add(user);
        userMatches.add(match);
        matchRepository.updateMatch(match, match.getId());
        userRepository.save(user);
        SportMatch newMatch = matchRepository.getReferenceById(match.getId());
        for(User player : newMatch.getPlayers()) {
            UserDTO player2 = userMapper.UsertoUserDTO(player);
        }

    }

    public ArrayList<MatchDTO> getAllMatches() {
        ArrayList<SportMatch> sportMatches = matchRepository.getAllMatches();
        return matchMapper.SportMatchestoMatchesDTO(sportMatches);
    }

    public ArrayList<MatchDTO> myMatches(String username) {
        ArrayList<SportMatch> sportMatches = matchRepository.getMyMatches(username);
        return matchMapper.SportMatchestoMatchesDTO(sportMatches);
    }
}
