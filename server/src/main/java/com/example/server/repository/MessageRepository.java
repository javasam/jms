package com.example.server.repository;

import com.example.server.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Long> {

    Optional<Message> findAllByBankAccountClientId(String id);
}
