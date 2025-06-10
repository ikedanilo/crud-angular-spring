package com.danilo.crud_angular_spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data // Define automaticamente Getters e Setters
@Entity // Entidade JPA
@Table(name = "produtos") // Define e indica o nome da tabela
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Chave primária e incrementada automaticamente
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;

    private Double preco;


}
