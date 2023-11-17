
package MyProjects.Accede.dto.location;


import MyProjects.Accede.dto.match.MatchDTO;
import lombok.Data;

import java.util.List;

@Data
public class LocationDTO {
    private String name;
    private String address;
    private List<MatchDTO> sportMatches;


}
