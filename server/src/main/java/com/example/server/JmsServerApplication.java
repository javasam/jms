package com.example.server;

import com.example.server.service.Impl.JmsReceiverImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class JmsServerApplication implements ApplicationRunner {

    @Autowired
    JmsReceiverImpl jmsReceiver;


    @Value("${jsa.activemq.queue}")
    private String request_queue;

    @Value("${jsa.activemq.queue2}")
    private String response_queue;

    public static void main(String[] args) {
        SpringApplication.run(JmsServerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            while (true) {
                Thread.sleep(1000);
                jmsReceiver.receiveAndSend(request_queue, response_queue);
            }
        } catch (Exception ex) {
            log.error("exception: " + ex);
        }
    }
}
