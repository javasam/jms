package com.example.server.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MessageDTO implements Serializable {

    private String operations;
    private String bankAccountClientId;
    private String bankAccountTransferId;
    private String bankClientName;
    private String amount;
}
