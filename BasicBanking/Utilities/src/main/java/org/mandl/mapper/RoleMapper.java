package org.mandl.mapper;

import org.mandl.RoleDto;
import org.mandl.identity.IdentityRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    RoleDto identiyRoleToRoleDto(IdentityRole identityRole);
    IdentityRole roleDtoToIdentityRole(RoleDto roleDto);
}
