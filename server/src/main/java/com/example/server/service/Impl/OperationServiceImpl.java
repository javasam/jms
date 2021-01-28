package com.example.server.service.Impl;

import com.example.server.dto.MessageDTO;
import com.example.server.mapper.EntityDtoMapper;
import com.example.server.model.Message;
import com.example.server.repository.MessageRepository;
import com.example.server.service.OperationsService;
import com.example.server.utils.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public class OperationServiceImpl implements OperationsService {

    private final MessageRepository messageRepository;
    private final EntityDtoMapper mapper;

    @Override
    public ResponseCode addAmount(MessageDTO messageDTO) {
        log.debug("addAmount income messageDTO -> {}", messageDTO);
        Message message = messageRepository.findAllByBankAccountClientId(messageDTO.getBankAccountClientId())
                .orElse(null);
        log.debug("received message from repository: {}", message);
        if(message != null && messageDTO.getAmount() != null) {
                message.setAmount(message.getAmount().add(new BigDecimal(messageDTO.getAmount())));
                messageRepository.save(message);
            return ResponseCode.OK;
        } else {
            return ResponseCode.ERROR;
        }
    }

    @Override
    public ResponseCode withdraw(MessageDTO messageDTO) {
        log.debug("withdraw income messageDTO -> {}", messageDTO);
        ResponseCode responseCode;
        Message message = messageRepository.findAllByBankAccountClientId(messageDTO.getBankAccountClientId())
                .orElse(null);
        log.debug("received message from repository: {}", message);
        if (message != null) {
            BigDecimal subtract = message.getAmount().subtract(new BigDecimal(messageDTO.getAmount()));
            if (subtract.compareTo(new BigDecimal("0")) < 0) {
                message.setAmount(new BigDecimal("0"));
                responseCode = ResponseCode.WARNING;
            } else {
                message.setAmount(subtract);
                responseCode = ResponseCode.OK;
            }
            messageRepository.save(message);
        } else {
            responseCode = ResponseCode.ERROR;
        }
        return responseCode;
    }

    @Override
    @Transactional
    public ResponseCode transfer(MessageDTO messageDTO) {
        log.debug("income transfer messageDTO -> {}", messageDTO);
        ResponseCode responseCode;
        Message messageFrom = messageRepository.findAllByBankAccountClientId(messageDTO.getBankAccountClientId())
                .orElse(null);
        Message messageTo = messageRepository.findAllByBankAccountClientId(messageDTO.getBankAccountTransferId())
                .orElse(null);
        log.debug("received message from repository: {}, {}", messageFrom, messageTo);
        if (messageTo != null && messageFrom != null) {
            BigDecimal subtract = messageFrom.getAmount().subtract(new BigDecimal(messageDTO.getAmount()));
            if (subtract.compareTo(new BigDecimal("0")) >= 0) {
                messageFrom.setAmount(subtract);
                messageTo.setAmount(messageTo.getAmount().add(new BigDecimal(messageDTO.getAmount())));
                messageRepository.save(messageFrom);
                messageRepository.save(messageTo);
                responseCode = ResponseCode.OK;
            } else {
                responseCode = ResponseCode.ERROR;
            }
        } else {
            responseCode = ResponseCode.ERROR;
        }
        return responseCode;
    }

    @Override
    public ResponseCode createAccount(MessageDTO messageDTO) {
        log.debug("income createAccount messageDTO -> {}", messageDTO);
        ResponseCode responseCode;
        Message message = messageRepository.findAllByBankAccountClientId(messageDTO.getBankAccountClientId())
                .orElse(null);
        log.debug("received message from repository: {}", message);
        if (message == null) {
            messageRepository.save(mapper.fromDto(messageDTO));
            responseCode = ResponseCode.OK;
        } else {
            responseCode = ResponseCode.ERROR;
        }
        return responseCode;
    }

    @Override
    public ResponseCode closeAccount(MessageDTO messageDTO) {
        log.debug("income closeAccount messageDTO -> {}", messageDTO);
        ResponseCode responseCode;
        Message message = messageRepository.findAllByBankAccountClientId(messageDTO.getBankAccountClientId())
                .orElse(null);
        if (message != null && message.getAmount().compareTo(new BigDecimal("0")) == 0) {
            messageRepository.delete(message);
            responseCode = ResponseCode.OK;
        } else {
            responseCode = ResponseCode.ERROR;
        }
        return responseCode;
    }
}
