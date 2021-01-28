package com.example.server.mapper;

import com.example.server.dto.MessageDTO;
import com.example.server.model.Message;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EntityDtoMapper {

    public Message fromDto(MessageDTO messageDTO) {
        return Message
                .builder()
                .bankAccountClientId(messageDTO.getBankAccountClientId())
                .bankClientName(messageDTO.getBankClientName())
                .amount(new BigDecimal(messageDTO.getAmount()))
                .build();
    }
}
