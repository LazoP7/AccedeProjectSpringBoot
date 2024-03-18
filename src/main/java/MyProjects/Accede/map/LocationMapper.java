package MyProjects.Accede.map;


import MyProjects.Accede.dto.location.LocationDTO;
import MyProjects.Accede.entities.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.ArrayList;


@Mapper(
        componentModel = "spring",
        uses = {MatchMapper.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LocationMapper //mapper between location and locationDTO objects
{
    @Mapping(
            source = "sportMatches",
            target = "sportMatches"
    )
    LocationDTO locationToLocationDTO(Location location);

    Location locationDTOToLocation(LocationDTO locationDTO);

    ArrayList<LocationDTO> locationsToLocationsDTO(ArrayList<Location> locations);
}