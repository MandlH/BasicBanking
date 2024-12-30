package org.mandl.mapper;

import org.mandl.ClaimDto;
import org.mandl.identity.IdentityClaim;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClaimMapper {
    ClaimMapper INSTANCE = Mappers.getMapper(ClaimMapper.class);
    ClaimDto identityClaimToClaimDto(IdentityClaim identityClaim);
    IdentityClaim claimDtoToIdentityClaim(ClaimDto claimDto);
}

