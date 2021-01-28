package com.example.server.service.Impl;

import com.example.server.dto.MessageDTO;
import com.example.server.mapper.EntityDtoMapper;
import com.example.server.model.Message;
import com.example.server.repository.MessageRepository;
import com.example.server.utils.ResponseCode;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.Silent.class)
public class OperationServiceImplTest extends TestCase {

    @InjectMocks
    OperationServiceImpl operationService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    EntityDtoMapper mapper;

    @Test
    public void addNotNullAmountTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount("2000")
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("2000"))
                        .bankAccountClientId("123456")
                        .bankClientName("Ivan")
                        .build()));
        assertEquals(ResponseCode.OK, operationService.addAmount(messageDTO));
    }

    @Test
    public void addNullAmountTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount(null)
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("2000"))
                        .bankAccountClientId("123456")
                        .bankClientName("Ivan")
                        .build()));
        assertEquals(ResponseCode.ERROR, operationService.addAmount(messageDTO));
    }

    @Test
    public void addAmountWhenMessageNullTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount(null)
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        Message message = null;
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.ofNullable(message));
        assertEquals(ResponseCode.ERROR, operationService.addAmount(messageDTO));
    }

    @Test
    public void addAmountWhenMessageEmptyTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount(null)
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.of(
                Message.builder().build()));
        assertEquals(ResponseCode.ERROR, operationService.addAmount(messageDTO));
    }

    @Test
    public void withdrawTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount("2000")
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("2000"))
                        .bankAccountClientId("123456")
                        .bankClientName("Ivan")
                        .build()));
        assertEquals(ResponseCode.OK, operationService.withdraw(messageDTO));
    }

    @Test
    public void withdrawWhenAmountLessTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount("2000")
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("1000"))
                        .bankAccountClientId("123456")
                        .bankClientName("Ivan")
                        .build()));
        assertEquals(ResponseCode.WARNING, operationService.withdraw(messageDTO));
    }

    @Test
    public void withdrawErrorTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount("2000")
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        assertEquals(ResponseCode.ERROR, operationService.withdraw(messageDTO));
    }

    @Test
    public void transferTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount("1000")
                .bankAccountClientId("123456")
                .bankAccountTransferId("123457")
                .bankClientName("Ivan")
                .build();
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("2000"))
                        .bankAccountClientId("123456")
                        .bankClientName("Ivan")
                        .build()));
        when(messageRepository.findAllByBankAccountClientId("123457")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("1000"))
                        .bankAccountClientId("123457")
                        .bankClientName("Petr")
                        .build()));
        assertEquals(ResponseCode.OK, operationService.transfer(messageDTO));
    }

    @Test
    public void transferWhenAmountMoreTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount("10000")
                .bankAccountClientId("123456")
                .bankAccountTransferId("123457")
                .bankClientName("Ivan")
                .build();
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("2000"))
                        .bankAccountClientId("123456")
                        .bankClientName("Ivan")
                        .build()));
        when(messageRepository.findAllByBankAccountClientId("123457")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("1000"))
                        .bankAccountClientId("123457")
                        .bankClientName("Petr")
                        .build()));
        assertEquals(ResponseCode.ERROR, operationService.transfer(messageDTO));
    }

    @Test
    public void createAccountTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount("2000")
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        Message message = null;
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.ofNullable(message));
        when(mapper.fromDto(any(MessageDTO.class))).thenCallRealMethod();
        assertEquals(ResponseCode.OK, operationService.createAccount(messageDTO));
    }

    @Test
    public void closeAccountWhenAmountNotEmptyTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount("2000")
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("2000"))
                        .bankAccountClientId("123456")
                        .bankClientName("Ivan")
                        .build()));
        doNothing().when(messageRepository).delete(any(Message.class));
        assertEquals(ResponseCode.ERROR, operationService.closeAccount(messageDTO));
    }

    @Test
    public void closeAccountWhenAmountEmptyTest() {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .amount("2000")
                .bankAccountClientId("123456")
                .bankClientName("Ivan")
                .build();
        when(messageRepository.findAllByBankAccountClientId("123456")).thenReturn(Optional.of(
                Message
                        .builder()
                        .amount(new BigDecimal("0"))
                        .bankAccountClientId("123456")
                        .bankClientName("Ivan")
                        .build()));
        doNothing().when(messageRepository).delete(any(Message.class));
        assertEquals(ResponseCode.OK, operationService.closeAccount(messageDTO));
    }
}