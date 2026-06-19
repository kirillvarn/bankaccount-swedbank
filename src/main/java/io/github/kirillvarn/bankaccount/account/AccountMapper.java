package io.github.kirillvarn.bankaccount.account;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import io.github.kirillvarn.bankaccount.Mapper;

@Component
public class AccountMapper implements Mapper<Account, AccountDto> {
    private ModelMapper modelMapper;

    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AccountDto mapTo(Account acc) {
        return modelMapper.map(acc, AccountDto.class);
    }

    @Override
    public Account mapFrom(AccountDto dto) {
        return modelMapper.map(dto, Account.class);
    }

}
