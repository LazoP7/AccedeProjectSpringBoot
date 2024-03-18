
package MyProjects.Accede.map;

import MyProjects.Accede.dto.match.MatchDTO;
import MyProjects.Accede.entities.Location;
import MyProjects.Accede.entities.SportMatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.ArrayList;

@Mapper(
        componentModel = "spring",
        uses = {PlayerMapper.class, LocationNameMapper.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MatchMapper //mapper between match and matchDTO objects
{
    SportMatch MatchDTOtoSportMatch(MatchDTO matchDTO);

    String locationToLocationName(Location location);

    @Mapping(source = "location", target = "locationName")
    @Mapping(source = "open", target = "open")
    MatchDTO SportMatchtoMatchDTO (SportMatch sportMatch);

    ArrayList<MatchDTO> SportMatchestoMatchesDTO (ArrayList<SportMatch> sportMatches);
}
