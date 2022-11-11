package hu.bme.hit.vihima06.caffshop.backend.mapper;

import hu.bme.hit.vihima06.caffshop.backend.model.Role;
import hu.bme.hit.vihima06.caffshop.backend.model.User;
import hu.bme.hit.vihima06.caffshop.backend.models.LoginResponseUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    LoginResponseUserDetails userToUserDetailsResponse(User user);

    default List<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(role -> role.getName().name()).toList();
    }
}