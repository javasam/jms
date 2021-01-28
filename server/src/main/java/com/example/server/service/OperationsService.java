package com.example.server.service;

import com.example.server.dto.MessageDTO;
import com.example.server.utils.ResponseCode;

public interface OperationsService {
    ResponseCode addAmount(MessageDTO messageDTO);
    ResponseCode withdraw(MessageDTO messageDTO);
    ResponseCode transfer(MessageDTO messageDTO);
    ResponseCode createAccount(MessageDTO messageDTO);
    ResponseCode closeAccount(MessageDTO messageDTO);
}
