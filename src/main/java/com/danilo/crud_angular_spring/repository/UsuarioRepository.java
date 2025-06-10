package com.danilo.crud_angular_spring.repository;

// Importações necessárias para o funcionamento do nosso repositório.
import java.util.Optional; // Usado para encapsular o resultado de uma busca, indicando que pode ou não haver um valor.

import org.springframework.data.jpa.repository.JpaRepository; // Interface fundamental do Spring Data JPA.
import org.springframework.stereotype.Repository; // Anotação para indicar que esta interface é um componente de repositório.

import com.danilo.crud_angular_spring.model.Usuario; // Importa a classe de modelo (entidade) Usuario.

/**
 * Esta interface, 'UsuarioRepository', é um **repositório de dados** para a entidade 'Usuario'.
 *
 * Assim como no 'ProdutoRepository', ao estender 'JpaRepository', o Spring Data JPA
 * automaticamente fornece uma ampla gama de métodos CRUD (Create, Read, Update, Delete)
 * e outras funcionalidades para interagir com o banco de dados relacionadas à entidade 'Usuario'.
 *
 * **@Repository**: Esta anotação, embora não seja estritamente obrigatória para 'JpaRepository'
 * funcionar, é uma boa prática. Ela indica que esta interface é um componente que lida com o acesso a dados
 * e permite que o Spring gerencie suas exceções de persistência de forma mais robusta.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Ao estender JpaRepository<Usuario, Long>, esta interface "herda" automaticamente
    // todos os métodos CRUD básicos para a entidade Usuario, como:
    // - save(Usuario usuario): Salva ou atualiza um usuário.
    // - findById(Long id): Busca um usuário pelo ID.
    // - findAll(): Retorna uma lista de todos os usuários.
    // - deleteById(Long id): Exclui um usuário pelo ID.
    // E muitos outros!

    /**
     * Este é um **método de consulta personalizado**.
     *
     * O Spring Data JPA é muito inteligente e pode criar a implementação de métodos
     * apenas pela sua assinatura (nome do método). Ao seguir uma convenção de nomes
     * (como 'findBy', 'Containing', 'IgnoreCase'), o Spring Data JPA consegue
     * gerar a consulta SQL apropriada para você.
     *
     * - **findByUsername**: Indica que queremos encontrar um usuário **pelo** seu campo 'username'.
     * O Spring Data JPA irá gerar uma consulta SQL similar a: `SELECT * FROM usuario WHERE username = ?`.
     *
     * - **Optional<Usuario>**: O tipo de retorno 'Optional' é uma boa prática no Java 8+
     * para indicar que o resultado da busca **pode ou não existir**. Isso ajuda a evitar
     * 'NullPointerExceptions', forçando você a verificar se o valor está presente
     * antes de tentar usá-lo (por exemplo, `usuarioOptional.isPresent()` ou `usuarioOptional.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"))`).
     *
     * @param username O nome de usuário a ser buscado no banco de dados.
     * @return Um 'Optional' contendo o objeto 'Usuario' se um usuário com o 'username'
     * especificado for encontrado, ou um 'Optional' vazio caso contrário.
     */
    Optional<Usuario> findByUsername(String username);

}