package com.example.jmsclient.service.impl;

import com.example.jmsclient.dto.MessageDTO;
import com.example.jmsclient.service.JmsSender;
import com.example.jmsclient.utils.ResponseCode;
import com.example.jmsclient.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class JmsSenderImpl implements JmsSender {

    private final JmsTemplate jmsTemplate;
    private final MessageConverter messageConverter;

    @Override
    public String sendAndReceiveMessage(String requestQueue, String responseQueue, MessageDTO messageDTO) {
        log.debug("sending message, to queue: {}, {}", messageDTO, requestQueue);
        UUID uuid = UUID.randomUUID();
        ResponseMessage response = new ResponseMessage();
        MessagePostProcessor messagePostProcessor = message -> {
            message.setJMSCorrelationID(uuid.toString());
            message.setStringProperty("_type", "com.example.server.dto.MessageDTO");
            return message;
        };
        jmsTemplate.convertAndSend(requestQueue, messageDTO, messagePostProcessor);

        jmsTemplate.setReceiveTimeout(10000);
        Message message = jmsTemplate.receiveSelected(responseQueue, "JMSCorrelationID='" + uuid.toString() + "'");
        try {
            if (message != null) {
                response = (ResponseMessage) messageConverter.fromMessage(message);
            } else {
                response.setCode(ResponseCode.ERROR);
            }
        } catch (JMSException jmsException) {
            log.error("JMSException: " + jmsException);
        }
        return response.getCode().getDescription();
    }
}
