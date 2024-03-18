
package MyProjects.Accede.map;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import MyProjects.Accede.dto.user.PlayerDTO;
import MyProjects.Accede.entities.User;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PlayerMapper //mapper between user and playerDTO objects
{
    PlayerDTO UserToPlayerDTO(User user);
}
