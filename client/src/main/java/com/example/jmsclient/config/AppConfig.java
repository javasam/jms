package com.example.jmsclient.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerURI;

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURI);
        List<String> trustedPackages = new ArrayList<>();
        trustedPackages.add("com.example.jmsdemo.dto.MessageDTO");
        activeMQConnectionFactory.setTrustedPackages(trustedPackages);
        return activeMQConnectionFactory;
    }

    @Bean //Bean for serialize object to json
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setMessageConverter(jacksonJmsMessageConverter());
        template.setConnectionFactory(connectionFactory());
        return template;
    }
}
