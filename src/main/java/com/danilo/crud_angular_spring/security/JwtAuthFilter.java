package com.danilo.crud_angular_spring.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Esta classe é um filtro de segurança JWT (JSON Web Token).
 * Ela intercepta todas as requisições HTTP para validar o token JWT
 * enviado no cabeçalho 'Authorization'. Se o token for válido, ela
 * autentica o usuário no contexto de segurança do Spring Security,
 * permitindo que ele acesse recursos protegidos.
 *
 * Estende 'OncePerRequestFilter' para garantir que este filtro seja
 * executado apenas uma vez para cada requisição HTTP, evitando trabalho redundante.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Injeção de dependência: o UserDetailsService é usado para carregar os detalhes do usuário
    // (como as autoridades/papéis) a partir do nome de usuário extraído do token JWT.
    private final UserDetailsService userDetailsService;

     /**
     * Construtor para injetar a dependência 'UserDetailsService'.
     * O Spring irá injetar uma implementação de 'UserDetailsService' (geralmente
     * a sua implementação personalizada que carrega usuários do banco de dados).
     * @param userDetailsService O serviço responsável por carregar os detalhes do usuário.
     */
    public JwtAuthFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Este é o método principal do filtro, executado para cada requisição HTTP.
     * Ele contém a lógica para extrair, validar e autenticar o token JWT.
     *
     * @param request O objeto HttpServletRequest que contém a requisição HTTP.
     * @param response O objeto HttpServletResponse que contém a resposta HTTP.
     * @param filterChain O FilterChain que representa a cadeia de filtros.
     * @throws ServletException Em caso de erros relacionados ao servlet.
     * @throws IOException Em caso de erros de entrada/saída.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Tenta obter o cabeçalho 'Authorization' da requisição.
        // É onde esperamos encontrar o token JWT, geralmente no formato "Bearer <TOKEN>".
        String authHeader = request.getHeader("Authorization");

        // 2. Verifica se o cabeçalho de autorização existe e começa com "Bearer ".
        // Se não houver token ou o formato for inválido, o filtro continua
        // para o próximo filtro na cadeia sem tentar autenticar.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Passa a requisição para o próximo filtro/recurso.
            return; // Sai do método, não há token JWT para processar.
        }

        // 3. Extrai o token JWT (removendo o prefixo "Bearer ").
        String token = authHeader.substring(7); // "Bearer " tem 7 caracteres.

        // 4. Extrai o nome de usuário (subject) do token JWT usando a utilidade JwtUtil.
        String username = JwtUtil.extractUsername(token);
        
        // 5. Verifica se o nome de usuário foi extraído com sucesso do token
        // e se o usuário atual ainda não está autenticado no contexto de segurança do Spring.
        // A segunda condição evita re-autenticação se o usuário já estiver autenticado.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Carrega os detalhes do usuário (UserDetails) a partir do nome de usuário.
            // Isso geralmente envolve consultar o banco de dados para obter as informações
            // do usuário, incluindo suas autoridades (papéis/permissões).
            UserDetails userdetails = userDetailsService.loadUserByUsername(username);

            // 7. Valida o token JWT.
            // Esta validação inclui verificar a assinatura, a expiração e se o token
            // corresponde aos detalhes do usuário carregados.
            if(JwtUtil.validateToken(token)) {

                // 8. Se o token for válido, cria um objeto de autenticação para o Spring Security.
                // 'UsernamePasswordAuthenticationToken' é uma classe padrão para representar a autenticação.
                // Os argumentos são: principal (o UserDetails), credenciais (null para JWT após a validação), e autoridades.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userdetails, null, userdetails.getAuthorities());
                
                // 9. Define o objeto de autenticação no contexto de segurança do Spring.
                // Isso significa que o usuário agora está autenticado para esta requisição,
                // e o Spring Security saberá quem é o usuário e quais são suas permissões
                // para autorizar o acesso a recursos protegidos (@PreAuthorize, etc.).
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            // 10. Continua a cadeia de filtros.
            // Passa a requisição para o próximo filtro na cadeia do Spring Security
            // ou para o dispatcher servlet se este for o último filtro de segurança.
            filterChain.doFilter(request, response);
        }
    }
}
