package com.example.Banking.user.Account;

import com.example.Banking.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data @Entity
@EqualsAndHashCode
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Min(0)
    private BigDecimal balance;

    @NotNull
    private Long userId;

    @NotNull
    private AccountType type;

    @Column(unique = true)
    private String name;

}
