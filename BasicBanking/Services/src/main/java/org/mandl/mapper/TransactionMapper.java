package org.mandl.mapper;

import org.mandl.TransactionDto;
import org.mandl.TransactionTypeDto;
import org.mandl.entities.Transaction;
import org.mandl.entities.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    TransactionDto domainToDto(Transaction transaction);
    Transaction dtoToDomain(TransactionDto transactionDto);
    TransactionTypeDto domainToDto(TransactionType transactionType);
    TransactionType dtoToDomain(TransactionTypeDto transactionTypeDto);
}
