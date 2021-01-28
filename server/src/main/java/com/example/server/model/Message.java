package com.example.server.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@EqualsAndHashCode
@Table(name="accounts")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="account_id")
    private String bankAccountClientId;

    @Column(name="client_name")
    private String bankClientName;

    @Column(name="amount")
    private BigDecimal amount;
}
