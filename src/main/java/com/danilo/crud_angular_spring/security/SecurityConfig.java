package com.danilo.crud_angular_spring.security;

// Importações de componentes e classes de configuração do Spring Security.
import org.springframework.context.annotation.Bean; // Usado para declarar um método produtor de um bean gerenciado pelo Spring.
import org.springframework.context.annotation.Configuration; // Anotação que indica que uma classe declara um ou mais métodos @Bean.
import org.springframework.security.authentication.AuthenticationManager; // Interface central para o processo de autenticação.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Ajuda na configuração do AuthenticationManager.
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Classe principal para configurar a segurança baseada em web.
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Habilita a integração do Spring Security com a configuração da web.
import org.springframework.security.config.http.SessionCreationPolicy; // Enum que define as políticas de criação de sessão.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Implementação de PasswordEncoder que usa o algoritmo BCrypt.
import org.springframework.security.crypto.password.PasswordEncoder; // Interface para codificação de senhas.
import org.springframework.security.web.SecurityFilterChain; // Representa a cadeia de filtros de segurança para uma requisição HTTP.
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filtro padrão do Spring Security para autenticação baseada em usuário/senha de formulário.

/**
 * Esta classe é a principal configuração de segurança da aplicação Spring Boot.
 * Ela utiliza o Spring Security para definir as regras de autenticação e autorização,
 * bem como a integração com JWT (JSON Web Tokens).
 *
 * @Configuration: Informa ao Spring que esta classe contém métodos que criam e configuram "beans" (objetos gerenciados pelo Spring).
 * @EnableWebSecurity: Ativa o suporte ao Spring Security em sua aplicação web, permitindo personalizar as configurações de segurança.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injeta o filtro JWT personalizado que criamos (JwtAuthFilter).
    // O Spring irá procurar por um bean de JwtAuthFilter e injetá-lo aqui.
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Construtor da classe SecurityConfig.
     * O Spring injeta automaticamente o JwtAuthFilter quando cria uma instância desta classe.
     * @param jwtAuthFilter O filtro JWT personalizado que será adicionado à cadeia de segurança.
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Define a cadeia de filtros de segurança para requisições HTTP.
     * Este é o método central onde você configura as regras de autorização,
     * gerenciamento de sessão e adiciona filtros personalizados.
     *
     * @param http Objeto HttpSecurity usado para configurar a segurança da web.
     * @return Uma SecurityFilterChain configurada.
     * @throws Exception Em caso de erro na configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 1. Desativa a proteção CSRF (Cross-Site Request Forgery).
        // Para APIs RESTful que usam JWT para autenticação, o CSRF geralmente não é necessário,
        // pois os tokens são stateless e enviados em cada requisição, em vez de depender de cookies de sessão.
        http.csrf(csrf -> csrf.disable());

        // 2. Configura o gerenciamento de sessão como STATELESS.
        // Isso significa que o Spring Security não criará ou usará sessões HTTP para armazenar
        // o estado do usuário. Cada requisição deve conter o token JWT para autenticação,
        // o que é ideal para APIs RESTful e microserviços.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 3. Define as regras de autorização para diferentes URIs.
        http.authorizeHttpRequests(auth -> auth
                // Permite acesso irrestrito (permitAll()) a todas as requisições que começam com "/auth/**".
                // Isso geralmente inclui endpoints de login e registro, que não exigem autenticação prévia.
                .requestMatchers("/auth/**").permitAll()
                // Todas as outras requisições (anyRequest()) devem ser autenticadas (authenticated()).
                // Isso significa que um token JWT válido é necessário para acessar qualquer outra rota.
                .anyRequest().authenticated());

        // 4. Adiciona o filtro JWT personalizado à cadeia de filtros do Spring Security.
        // Ele é adicionado ANTES (addFilterBefore) do UsernamePasswordAuthenticationFilter.class.
        // Isso garante que nosso filtro JWT seja executado primeiro para validar o token
        // antes que o filtro padrão de autenticação por formulário do Spring Security tente processar a requisição.
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // 5. Constrói e retorna a cadeia de filtros de segurança configurada.
        return http.build();
    }

    /**
     * Expõe o AuthenticationManager como um bean Spring.
     * O AuthenticationManager é usado para processar requisições de autenticação,
     * como no endpoint de login, onde você autentica as credenciais do usuário.
     *
     * @param config A configuração de autenticação do Spring Security.
     * @return Uma instância do AuthenticationManager.
     * @throws Exception Se houver um erro ao obter o AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Delega a criação do AuthenticationManager ao Spring.
        // Ele usará as configurações padrão e quaisquer UserDetailsService configurados
        // para construir o gerenciador.
        return config.getAuthenticationManager();
    }

    /**
     * Define o PasswordEncoder a ser usado para criptografar e verificar senhas.
     *
     * @return Uma instância de BCryptPasswordEncoder.
     * BCrypt é um algoritmo de hash de senha forte e recomendado.
     * É crucial usar um PasswordEncoder para armazenar senhas de forma segura
     * no banco de dados (nunca armazene senhas em texto puro!).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}