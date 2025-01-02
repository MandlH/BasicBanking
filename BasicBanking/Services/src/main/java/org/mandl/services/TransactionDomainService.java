package org.mandl.services;

import jakarta.inject.Inject;
import org.h2.mvstore.tx.TransactionMap;
import org.hibernate.service.spi.ServiceException;
import org.mandl.LoggingHandler;
import org.mandl.TransactionDto;
import org.mandl.TransactionService;
import org.mandl.TransactionTypeDto;
import org.mandl.mapper.BankAccountMapper;
import org.mandl.mapper.TransactionMapper;
import org.mandl.repositories.RepositoryWrapper;
import org.mandl.repositories.TransactionRepository;

import java.util.List;
import java.util.UUID;

final class TransactionDomainService implements TransactionService {
    private static final LoggingHandler logger = LoggingHandler.getLogger(BankAccountDomainService.class);
    private final TransactionRepository transactionRepository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public TransactionDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.transactionRepository = repositoryWrapper.getTransactionRepository();
    }

    @Override
    public List<TransactionDto> getTransactions(TransactionTypeDto typeDto, UUID bankAccountId) {
        try {
            var transactionType = TransactionMapper.INSTANCE.dtoToDomain(typeDto);
            var transactions = transactionRepository.getTransactions(transactionType, bankAccountId);
            return transactions.stream()
                    .map(TransactionMapper.INSTANCE::domainToDto)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("Error retrieving Transactions", e);
        }
    }

    @Override
    public List<TransactionDto> getTransactions(UUID bankAccountId) {
        try {
            var transactions = transactionRepository.getTransactions(bankAccountId);
            return transactions.stream()
                    .map(TransactionMapper.INSTANCE::domainToDto)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("Error retrieving Transactions", e);
        }
    }
}
