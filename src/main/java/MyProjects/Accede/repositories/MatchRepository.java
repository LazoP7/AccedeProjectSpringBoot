
package MyProjects.Accede.repositories;

import java.util.ArrayList;
import java.util.Calendar;

import MyProjects.Accede.entities.SportMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<SportMatch, Integer> {
    @Query("SELECT match FROM SportMatch match")
    ArrayList<SportMatch> getAllMatches();

    @Modifying
    @Query("DELETE FROM SportMatch match WHERE match.date = :date")
    void deleteByDate(@Param("date") Calendar date);

    @Modifying
    @Query(
            value = "INSERT INTO sport_match (date, location_id, num_of_players) VALUES (:date, :locationId, :numOfPlayers)",
            nativeQuery = true
    )
    void createMatch(@Param("date") Calendar date, @Param("locationId") Integer locationId, @Param("numOfPlayers") int numOfPlayers);

    @Query("SELECT match FROM SportMatch match WHERE match.location.name = :locationName AND FUNCTION('MONTH', match.date) = FUNCTION('MONTH', :day) AND FUNCTION('DAY', match.date) = FUNCTION('DAY', :day) AND FUNCTION('YEAR', match.date) = FUNCTION('YEAR', :day)")
    ArrayList<SportMatch> getByLocationAndDay(@Param("locationName") String locationName, @Param("day") Calendar day);

    @Query("SELECT match FROM SportMatch match WHERE location.name = ?1")
    ArrayList<SportMatch> findByLocation(String location);

    @Query("SELECT match FROM SportMatch match WHERE DATE(date) = :day")
    ArrayList<SportMatch> getMatchByDate(@Param("day") Calendar day);

    @Query("SELECT match FROM SportMatch match " +
            "WHERE location.name = :locationName " +
            "AND STR_TO_DATE(CONCAT(match.date, ' ', HOUR(match.date), ':', MINUTE(match.date), ':', SECOND(match.date)), '%Y-%m-%d %H:%i:%s') = :dateTime")
    SportMatch getMatchByDateAndTime(@Param("locationName") String locationName, @Param("dateTime") String dateTime);

    @Modifying
    @Query("UPDATE SportMatch match SET match = ?1 WHERE match.id = ?2")
    void updateMatch(SportMatch newMatch, Integer matchId);

    @Query("SELECT DISTINCT sm FROM SportMatch sm JOIN sm.players p WHERE p.username = :username")
    ArrayList<SportMatch> getMyMatches(@Param("username") String username);
}