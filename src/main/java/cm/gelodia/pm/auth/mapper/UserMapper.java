package cm.gelodia.pm.auth.mapper;

import cm.gelodia.pm.auth.dto.UserDto;
import cm.gelodia.pm.auth.model.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {
    User map(UserDto userDto);
    UserDto map(User user);
}
