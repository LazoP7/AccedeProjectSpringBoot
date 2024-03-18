
package MyProjects.Accede.dto.match;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import MyProjects.Accede.dto.location.LocationNameDTO;
import MyProjects.Accede.dto.user.PlayerDTO;
import lombok.Data;

@Data
public class MatchDTO {
    private Integer id;
    private Calendar date;
    private Integer num_of_players;
    private LocationNameDTO locationName;
    private int type;
    private boolean open;
    private Set<PlayerDTO> players = new HashSet<>();

}