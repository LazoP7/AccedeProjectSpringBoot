
package MyProjects.Accede.map;

import MyProjects.Accede.dto.match.MatchDTO;
import MyProjects.Accede.entities.Location;
import MyProjects.Accede.entities.SportMatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import MyProjects.Accede.map.PlayerMapper;
import java.util.ArrayList;

@Mapper(
        componentModel = "spring",
        uses = {PlayerMapper.class, LocationNameMapper.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MatchMapper {
    SportMatch MatchDTOtoSportMatch(MatchDTO matchDTO);

    String locationToLocationName(Location location);

    @Mapping(source = "location", target = "locationName")
    MatchDTO SportMatchtoMatchDTO(SportMatch sportMatch);

    ArrayList<MatchDTO> SportMatchestoMatchesDTO(ArrayList<SportMatch> sportMatches);
}
