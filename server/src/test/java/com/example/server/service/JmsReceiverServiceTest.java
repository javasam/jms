package com.example.server.service;

import com.example.server.dto.MessageDTO;
import com.example.server.service.Impl.JmsReceiverImpl;
import com.example.server.service.Impl.OperationServiceImpl;
import com.example.server.utils.ResponseCode;
import com.example.server.utils.ResponseMessage;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class JmsReceiverServiceTest extends TestCase {

    @InjectMocks
    JmsReceiverImpl jmsReceiver;

    @Mock
    JmsTemplate jmsTemplate;

    @Mock
    MessageConverter messageConverter;

    @Mock
    Message message;

    @Mock
    OperationServiceImpl operationService;

    @Captor
    ArgumentCaptor<String> responseQueueCaptor;
    @Captor
    ArgumentCaptor<ResponseMessage> responseMessageCaptor;

    @Test
    public void receiveAndSendTest() {
        String requestQueue = "123";
        String responseQueue = "124";
        MessageDTO messageDTO = MessageDTO
                .builder()
                .operations("ADD_AMOUNT")
                .bankClientName("Ivan")
                .bankAccountClientId("123456")
                .amount("2000")
                .build();
        try {
            when(messageConverter.fromMessage(message)).thenReturn(messageDTO);
        } catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }
        when(jmsTemplate.receive(anyString())).thenReturn(message);
        when(operationService.addAmount(any(MessageDTO.class))).thenReturn(ResponseCode.OK);
        jmsReceiver.receiveAndSend(requestQueue, responseQueue);
        verify(operationService, times(1)).addAmount(messageDTO);
        verify(jmsTemplate).convertAndSend(responseQueueCaptor.capture(), responseMessageCaptor.capture(), any(MessagePostProcessor.class));
        assertEquals(responseQueue, responseQueueCaptor.getValue());
        assertEquals(ResponseCode.OK.toString(), responseMessageCaptor.getValue().getCode());
    }
}
