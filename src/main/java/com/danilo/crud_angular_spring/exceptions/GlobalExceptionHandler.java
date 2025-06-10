package com.danilo.crud_angular_spring.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Esta classe é responsável por centralizar o tratamento de exceções em toda a aplicação Spring Boot.
 * Com a anotação @RestControllerAdvice, ela "escuta" as exceções que ocorrem nos @Controllers
 * e @RestControllers e as trata de forma padronizada, retornando respostas de erro mais amigáveis e informativas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Este método é um manipulador específico para a exceção 'RecursoNaoEncontradoException'.
     * Quando essa exceção é lançada em qualquer parte da sua aplicação (por exemplo, quando um ID não é encontrado no banco de dados),
     * este método é acionado automaticamente.
     *
     * @param ex A instância da exceção 'RecursoNaoEncontradoException' que foi lançada.
     * @return Retorna um ResponseEntity com um status HTTP 404 (Not Found) e um corpo JSON
     * contendo detalhes sobre o erro, como timestamp, status, tipo de erro e a mensagem da exceção.
     */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Object> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        // Cria um mapa para construir o corpo da resposta JSON de erro.
        // LinkedHashMap é usado para garantir a ordem dos campos no JSON.
        Map<String, Object> body = new LinkedHashMap<>();

        // Adiciona o timestamp (data e hora atual) ao corpo da resposta.
        body.put("timestamp", LocalDateTime.now());
        // Adiciona o código de status HTTP (404) ao corpo da resposta.
        body.put("status", HttpStatus.NOT_FOUND.value());
        // Adiciona uma mensagem genérica de erro ao corpo da resposta.
        body.put("error", "Recurso não encontrado");
        // Adiciona a mensagem específica da exceção (definida onde a exceção foi lançada) ao corpo da resposta.
        body.put("message", ex.getMessage());

        // Retorna um ResponseEntity contendo o mapa 'body' como corpo da resposta
        // e o status HTTP 404 (NOT_FOUND).
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


    /**
     * Este método é um manipulador genérico para qualquer outra exceção ('Exception.class')
     * que não tenha um manipulador mais específico definido (como o de 'RecursoNaoEncontradoException').
     * Ele atua como um "catch-all" para garantir que qualquer erro inesperado seja tratado de forma graciosa,
     * evitando que a aplicação retorne erros brutos ou stacks de erro para o cliente.
     *
     * @param ex A instância da exceção genérica 'Exception' que foi lançada.
     * @return Retorna um ResponseEntity com um status HTTP 500 (Internal Server Error) e um corpo JSON
     * contendo detalhes sobre o erro.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        // Cria um mapa para construir o corpo da resposta JSON de erro.
        Map<String, Object> body = new LinkedHashMap<>();

        // Adiciona o timestamp (data e hora atual) ao corpo da resposta.
        body.put("timestamp", LocalDateTime.now());
        // Adiciona o código de status HTTP (500) ao corpo da resposta.
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        // Adiciona uma mensagem genérica de erro ao corpo da resposta.
        body.put("error", "Erro interno do servidor");
        // Adiciona a mensagem específica da exceção ao corpo da resposta.
        body.put("message", ex.getMessage());

        // Retorna um ResponseEntity contendo o mapa 'body' como corpo da resposta
        // e o status HTTP 500 (INTERNAL_SERVER_ERROR).
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
