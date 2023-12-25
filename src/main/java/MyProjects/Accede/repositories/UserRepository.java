
package MyProjects.Accede.repositories;

import java.util.ArrayList;
import java.util.Set;

import MyProjects.Accede.entities.Role;
import MyProjects.Accede.entities.SportMatch;
import MyProjects.Accede.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select user from User user")
    ArrayList<User> getAllUsers();

    @Modifying
    @Query("UPDATE User u SET u.roles = :roles WHERE u.id = :userId")
    void updateUserRoles(@Param("userId") Integer userId, @Param("roles") Set<Role> roles);

    @Query("SELECT u.mySportMatches FROM User u WHERE u.id = :userId")
    Set<SportMatch> getMatchesForUser(@Param("userId") Integer userId);

    @Query("SELECT user FROM User user JOIN FETCH user.roles AS role WHERE user.username = :username")
    User findByUsername(@Param("username") String username);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.profDescr = :profDesc WHERE u.id = :userId")
    int updateDescr(@Param("userId") Integer userId, @Param("profDesc") String profDesc);
}
