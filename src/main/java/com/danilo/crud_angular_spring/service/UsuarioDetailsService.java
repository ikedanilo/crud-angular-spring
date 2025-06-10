package com.danilo.crud_angular_spring.service;

// Importações do Spring Security relacionadas a detalhes do usuário.
import org.springframework.security.core.userdetails.User; // Implementação padrão de UserDetails fornecida pelo Spring Security.
import org.springframework.security.core.userdetails.UserDetails; // Interface que representa os detalhes de um usuário (nome de usuário, senha, papéis, etc.).
import org.springframework.security.core.userdetails.UserDetailsService; // Interface principal para carregar dados específicos do usuário.
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exceção lançada quando um usuário não é encontrado.
import org.springframework.stereotype.Service; // Anotação que indica que esta classe é um componente de serviço gerenciado pelo Spring.

// Importações de modelos e repositórios da sua aplicação.
import com.danilo.crud_angular_spring.model.Usuario; // A entidade de usuário da sua aplicação.
import com.danilo.crud_angular_spring.repository.UsuarioRepository; // O repositório para acessar dados de usuário no banco de dados.

/**
 * Esta classe é uma implementação do 'UserDetailsService' do Spring Security.
 * Seu principal objetivo é carregar os detalhes de um usuário a partir do banco de dados
 * (ou de qualquer outra fonte de dados) quando uma tentativa de autenticação ocorre.
 *
 * O Spring Security usa esta classe para obter as informações necessárias (nome de usuário,
 * senha criptografada e papéis/autoridades) para validar as credenciais de login.
 *
 * @Service: Indica ao Spring que esta classe é um componente de serviço, ou seja,
 * ela contém lógica de negócio e pode ser injetada em outros componentes.
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    // Injeta o repositório de usuários, que é usado para buscar os dados do usuário no banco de dados.
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor para injetar a dependência 'UsuarioRepository'.
     * O Spring injeta automaticamente uma instância de 'UsuarioRepository' quando cria
     * uma instância desta classe.
     * @param usuarioRepository O repositório responsável por operações de CRUD de usuário.
     */
    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Este é o método principal exigido pela interface 'UserDetailsService'.
     * Ele é chamado pelo Spring Security sempre que ele precisa carregar os detalhes de um usuário,
     * tipicamente durante o processo de autenticação (por exemplo, quando o usuário tenta fazer login).
     *
     * @param username O nome de usuário fornecido na tentativa de autenticação.
     * @return Um objeto 'UserDetails' contendo as informações do usuário encontrado.
     * @throws UsernameNotFoundException Se nenhum usuário com o nome de usuário especificado for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Busca o usuário no banco de dados usando o repositório.
        // O método findByUsername retorna um Optional<Usuario>.
        // .orElseThrow() é usado para, se o Optional estiver vazio (usuário não encontrado),
        // lançar uma UsernameNotFoundException.
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username)); // Melhoria: inclua o username na mensagem para depuração.

        // 2. Constrói um objeto 'UserDetails' que o Spring Security pode entender.
        // Usamos a classe 'User.builder()' fornecida pelo Spring Security para criar uma instância de UserDetails.
        return User.builder()
                .username(usuario.getUsername()) // Define o nome de usuário.
                .password(usuario.getPassword()) // Define a senha (esta deve ser a senha CRIPTOGRAFADA do banco de dados).
                // Define os papéis (roles) ou autoridades do usuário.
                // Neste exemplo, todos os usuários são atribuídos ao papel "USER".
                // Em aplicações mais complexas, você buscaria os papéis do usuário no banco de dados.
                .roles("USER")
                .build(); // Constrói e retorna o objeto UserDetails.
    }

}