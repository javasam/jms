package com.example.jmsclient;

import com.example.jmsclient.dto.MessageDTO;
import com.example.jmsclient.service.JmsSender;
import com.example.jmsclient.utils.Operations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class JmsClientApplication implements ApplicationRunner {

    @Autowired
    private JmsSender jmsSender;

    @Value("${jsa.activemq.queue}")
    private String request_queue;

    @Value("${jsa.activemq.queue2}")
    private String response_queue;

    public static void main(String[] args) {
        SpringApplication.run(JmsClientApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments arg0) {
        String changer;
        Operations operation = Operations.EXIT;
        String bankAccountClientId;
        String bankAccountTransferId = null;
        String bankClientName;
        String amount;
        System.out.println("" +
                ",(((((((((,    ((((((((((()    .(((((((()     *(((((((((() \n" +
                "(((            (((      *(()   .(((           *(((      (()\n" +
                "/((((((/.      (((((((((((*    .((((((((()    *(((      (()\n" +
                "     ,(((((,   (((      /(()   .(((           *((((((((((( \n" +
                "(       /((/   (((       (((*  .(((           *(((   ,(((  \n" +
                "(((((((((#.    (((((((((((())   .(((((((((()   *(((     ((()     ");
        System.out.println("Press any key to start operations");
        try (Scanner in = new Scanner(System.in)) {
            while (!in.nextLine().equals("exit")) {
                System.out.println("Выберите операцию: \n\t" +
                        "1. Пополнение счета.\n\t" +
                        "2. Списание средств.\n\t" +
                        "3. Перевод другому клиенту.\n\t" +
                        "4. Создание счета.\n\t" +
                        "5. Закрытие счета.\n\t" +
                        "6. Выход");
                changer = in.nextLine();
                switch (changer) {
                    case "1":
                        operation = Operations.ADD_AMOUNT;
                        break;
                    case "2":
                        operation = Operations.WITHDRAW;
                        break;
                    case "3":
                        operation = Operations.TRANSFER;
                        break;
                    case "4":
                        operation = Operations.CREATE_ACCOUNT;
                        break;
                    case "5":
                        operation = Operations.CLOSE_ACCOUNT;
                        break;
                    case "6": {
                        System.exit(0);
                        break;
                    }
                    default:
                        operation = Operations.FATAL_ERROR;
                        break;
                }
                System.out.println("Выбрана операция: " + operation);
                if (operation.equals(Operations.TRANSFER)) {
                    System.out.println("Введите номер счета для перевода средств: ");
                    bankAccountTransferId = in.nextLine();
                }
                System.out.println("Введите номер вашего счета: ");
                bankAccountClientId = in.nextLine();
                System.out.println("Введите сумму операции: ");
                amount = in.nextLine();
                System.out.println("Введите ваше имя: ");
                bankClientName = in.nextLine();
                MessageDTO messageDTO = MessageDTO
                        .builder()
                        .operations(operation.name())
                        .bankAccountClientId(bankAccountClientId)
                        .bankAccountTransferId(bankAccountTransferId)
                        .bankClientName(bankClientName)
                        .amount(amount)
                        .build();
                System.out.println(jmsSender.sendAndReceiveMessage(request_queue, response_queue, messageDTO));
                log.debug("message sent to queue: {} {}", messageDTO, request_queue);
                System.out.println("Для совершения следующей операции нажмите любую клавишу");
            }
        } catch (Exception e) {
            System.err.println("Программа завершила работу с ошибкой: " + e);
        }
    }

}
