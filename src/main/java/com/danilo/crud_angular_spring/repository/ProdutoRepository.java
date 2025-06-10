package com.danilo.crud_angular_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.danilo.crud_angular_spring.model.Produto;

/**
 * Esta interface, 'ProdutoRepository', é um **repositório de dados** para a entidade 'Produto'.
 *
 * No Spring Data JPA, você não precisa escrever a implementação das operações de banco de dados.
 * Basta **estender a interface 'JpaRepository'**, e o Spring Data JPA automaticamente
 * fornece uma vasta gama de métodos CRUD (Create, Read, Update, Delete) e outras funcionalidades
 * para interagir com o banco de dados relacionadas à entidade 'Produto'.
 *
 * **@Repository**: Esta anotação é um estereótipo do Spring que indica que a classe
 * (ou, neste caso, a interface) é um componente que lida com o acesso a dados.
 * Embora não seja estritamente obrigatória para 'JpaRepository' funcionar (pois 'JpaRepository'
 * já é reconhecida pelo Spring), é uma boa prática para clareza e para que o Spring
 * possa aplicar tratamento de exceções específico de persistência, se necessário.
 */

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Ao estender JpaRepository<Produto, Long>, esta interface "herda" automaticamente
    // diversos métodos úteis para a entidade Produto, como:
    //
    // - save(Produto produto): Salva um novo produto ou atualiza um existente.
    // - findById(Long id): Busca um produto pelo seu ID. Retorna um Optional<Produto>.
    // - findAll(): Retorna uma lista de todos os produtos.
    // - deleteById(Long id): Exclui um produto pelo seu ID.
    // - count(): Retorna o número total de produtos.
    // - existsById(Long id): Verifica se um produto com o ID existe.
    //
    // E muitos outros! Você pode simplesmente "injetar" (autowire) esta interface
    // em seus serviços e controladores para começar a usar esses métodos.

    // Se você precisar de métodos de consulta mais específicos que não são cobertos
    // pelos métodos padrão do JpaRepository (por exemplo, buscar produtos por nome,
    // ou por faixa de preço), você pode declará-los aqui.
    //

    // Como você não adicionou nenhum método personalizado, esta interface já está
    // pronta para usar todas as operações CRUD básicas fornecidas pelo Spring Data JPA.
}
