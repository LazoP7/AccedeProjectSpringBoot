
package MyProjects.Accede.map;

import MyProjects.Accede.dto.user.UserDTO;
import MyProjects.Accede.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    User UserDTOtoUser(UserDTO userDTO);

    UserDTO UsertoUserDTO(User user);
}

