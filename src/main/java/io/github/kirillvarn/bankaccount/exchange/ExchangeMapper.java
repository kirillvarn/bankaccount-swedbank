package io.github.kirillvarn.bankaccount.exchange;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import io.github.kirillvarn.bankaccount.Mapper;

@Component
public class ExchangeMapper implements Mapper<Exchange, ExchangeDto> {
    private ModelMapper modelMapper;

    public ExchangeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ExchangeDto mapTo(Exchange acc) {
        return modelMapper.map(acc, ExchangeDto.class);
    }

    @Override
    public Exchange mapFrom(ExchangeDto dto) {
        return modelMapper.map(dto, Exchange.class);
    }

}
