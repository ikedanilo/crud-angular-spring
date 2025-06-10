package com.danilo.crud_angular_spring.security;

// Importações da biblioteca JJWT (Java JWT), que é a implementação que você está usando.
import io.jsonwebtoken.JwtException; // Exceção genérica para erros de JWT.
import io.jsonwebtoken.Jwts; // Classe principal para construir, analisar e validar JWTs.
import io.jsonwebtoken.SignatureAlgorithm; // Enum que representa os algoritmos de assinatura suportados.
import io.jsonwebtoken.security.Keys; // Classe utilitária para gerar chaves de segurança.

import java.security.Key; // Interface que representa uma chave criptográfica.
import java.util.Date; // Usado para lidar com datas de expiração.

/**
 * Esta é uma classe utilitária (utility class) responsável por todas as operações
 * relacionadas à criação, extração e validação de JSON Web Tokens (JWTs).
 *
 * Uma classe utilitária tipicamente contém apenas métodos estáticos e não deve
 * ser instanciada.
 */
public class JwtUtil {

    /**
     * Construtor privado para evitar a instanciação desta classe utilitária.
     * Lança uma UnsupportedOperationException para reforçar que ela não deve ser instanciada.
     */
    private JwtUtil() {
        throw new UnsupportedOperationException("Classe utilitária");
    }

    // Chave secreta estática e final para assinar e verificar a assinatura dos tokens JWT.
    // É crucial que esta chave seja mantida em segredo no lado do servidor.
    // 'Keys.secretKeyFor(SignatureAlgorithm.HS256)' gera uma chave segura para o algoritmo HS256.
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tempo de expiração do token em milissegundos.
    // 86400000 milissegundos = 24 horas (1 dia).
    // Este valor define por quanto tempo um token gerado será válido.
    private static final long EXPIRATION_TIME = 86400000; // 1 dia

    /**
     * Gera um novo JSON Web Token (JWT) para um determinado nome de usuário.
     *
     * @param username O nome de usuário que será o "subject" (assunto) do token.
     * @return Uma String contendo o JWT gerado.
     */
    public static String generateToken(String username) {
        return Jwts.builder() // Inicia a construção de um JWT.
                .setSubject(username) // Define o "subject" (assunto) do token, que é o nome de usuário.
                // Define a data de expiração do token: agora + EXPIRATION_TIME.
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // Assina o token usando a chave secreta gerada ('key') e o algoritmo HS256.
                .signWith(key, SignatureAlgorithm.HS256)
                .compact(); // Finaliza a construção e compacta o token em uma String.
    }

    /**
     * Extrai o nome de usuário (subject) de um token JWT.
     *
     * @param token A String do token JWT a ser analisado.
     * @return A String contendo o nome de usuário extraído do token.
     * @throws JwtException Se o token for inválido, corrompido ou a assinatura não corresponder.
     */
    public static String extractUsername(String token) {
        return Jwts.parserBuilder() // Inicia a análise de um JWT.
                .setSigningKey(key) // Define a chave secreta a ser usada para verificar a assinatura do token.
                .build() // Constrói o parser de JWT.
                .parseClaimsJws(token) // Analisa o token e verifica sua assinatura.
                .getBody() // Obtém o corpo (payload/claims) do token.
                .getSubject(); // Extrai o "subject" (nome de usuário) do corpo do token.
    }

    /**
     * Valida um token JWT, verificando sua assinatura e se ele não expirou.
     *
     * @param token A String do token JWT a ser validado.
     * @return true se o token for válido (assinatura correta e não expirado), false caso contrário.
     */
    public static boolean validateToken(String token) {
        try {
            // Tenta analisar o token. Se a assinatura for inválida ou o token tiver expirado,
            // uma JwtException será lançada.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; // Se não lançar exceção, o token é válido.
        }
        catch (JwtException _) {
            // Captura qualquer exceção relacionada a JWT (assinatura inválida, token expirado, etc.)
            // e retorna false, indicando que o token não é válido.
            return false;
        }
    }
}