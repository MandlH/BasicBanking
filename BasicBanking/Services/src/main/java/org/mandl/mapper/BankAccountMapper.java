package org.mandl.mapper;

import org.mandl.BankAccountDto;
import org.mandl.entities.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountMapper {
    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    BankAccountDto domainToDto(BankAccount bankAccount);

    BankAccount dtoToDomain(BankAccountDto bankAccountDto);
}

