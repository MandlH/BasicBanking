package org.mandl.mapper;

import org.mandl.UserDto;
import org.mandl.identity.IdentityUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto identityUserToUserDto(IdentityUser user);
    IdentityUser userDtoToIdentityUser(UserDto userDto);
}
