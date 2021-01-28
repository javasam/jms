package com.example.server.service.Impl;

import com.example.server.dto.MessageDTO;
import com.example.server.service.JmsReceiver;
import com.example.server.utils.ResponseCode;
import com.example.server.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@Service
@AllArgsConstructor
public class JmsReceiverImpl implements JmsReceiver {

    private final JmsTemplate jmsTemplate;
    private final OperationServiceImpl operationsService;
    private final MessageConverter messageConverter;

    public void receiveAndSend(String requestQueue, String responseQueue) {
        log.debug("message received from queue: {}", requestQueue);
        Message message = jmsTemplate.receive(requestQueue);
        log.debug("received message {}", message);
        String jmsCorrelationID = null;
        ResponseCode responseCode = ResponseCode.ERROR;
        MessageDTO messageDTO = new MessageDTO();
        try {
            if (message != null) {
                jmsCorrelationID = message.getJMSCorrelationID();
                messageDTO = (MessageDTO) messageConverter.fromMessage(message);
            }
        } catch (JMSException jmsException) {
            log.error("jmsException " + jmsException.getErrorCode());
        }
        switch (messageDTO.getOperations()) {
            case "ADD_AMOUNT":
                responseCode = operationsService.addAmount(messageDTO);
                break;
            case "WITHDRAW":
                responseCode = operationsService.withdraw(messageDTO);
                break;
            case "TRANSFER":
                responseCode = operationsService.transfer(messageDTO);
                break;
            case "CREATE_ACCOUNT":
                responseCode = operationsService.createAccount(messageDTO);
                break;
            case "CLOSE_ACCOUNT":
                responseCode = operationsService.closeAccount(messageDTO);
                break;
            default:
                System.exit(0);
        }
        createMessage(responseCode.toString(), responseQueue, jmsCorrelationID);
    }

    private void createMessage(String code, String responseQueue, String jmsCorrelationId) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(code);
        MessagePostProcessor messagePostProcessor = message -> {
            message.setJMSCorrelationID(jmsCorrelationId);
            message.setStringProperty("_type", "com.example.jmsclient.utils.ResponseMessage");
            return message;
        };
        jmsTemplate.convertAndSend(responseQueue, responseMessage, messagePostProcessor);
        log.debug("send response message, to queue: {} {}", responseMessage, responseQueue);
    }
}
