package io.github.kirillvarn.bankaccount.transaction;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import io.github.kirillvarn.bankaccount.Mapper;

@Component
public class TransactionMapper implements Mapper<Transaction, TransactionDto> {
    private ModelMapper modelMapper;

    public TransactionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TransactionDto mapTo(Transaction acc) {
        return modelMapper.map(acc, TransactionDto.class);
    }

    @Override
    public Transaction mapFrom(TransactionDto dto) {
        return modelMapper.map(dto, Transaction.class);
    }

}
