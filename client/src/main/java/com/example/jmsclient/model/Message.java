package com.example.jmsclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {

    private String id;
    private String bankAccountNumber;
    private String bankClientName;
    private BigDecimal amount;
}
