
package MyProjects.Accede.map;

import MyProjects.Accede.dto.location.LocationNameDTO;
import MyProjects.Accede.entities.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LocationNameMapper {
    LocationNameDTO locationNameToString(Location location);
}
