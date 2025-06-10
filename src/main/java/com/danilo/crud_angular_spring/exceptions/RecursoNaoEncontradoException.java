package com.danilo.crud_angular_spring.exceptions;

/**
 * Esta é uma classe de exceção personalizada que estende 'RuntimeException'.
 * Exceções de tempo de execução (RuntimeException) são exceções não checadas,
 * o que significa que o compilador Java não obriga você a declará-las em uma
 * cláusula 'throws' ou a envolvê-las em um bloco 'try-catch'.
 *
 * Nós a utilizamos para indicar que um recurso específico (por exemplo, um usuário,
 * um produto, um registro) não foi encontrado no sistema, geralmente em uma operação
 * de busca por ID no banco de dados ou em uma lista.
 *
 * Ao lançar esta exceção, nosso 'GlobalExceptionHandler' (a classe que comentamos
 * anteriormente) irá interceptá-la e retornar uma resposta HTTP 404 (Not Found)
 * para o cliente, indicando de forma clara que o recurso solicitado não existe.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    /**
     * O construtor desta exceção.
     *
     * @param mensagem Uma String que descreve o motivo pelo qual o recurso não foi encontrado.
     * Esta mensagem será exibida na resposta JSON de erro para o cliente.
     * Exemplo: "Recurso com ID 123 não encontrado."
     */
    public RecursoNaoEncontradoException(String mensagem) {
        // Chama o construtor da classe pai (RuntimeException) e passa a mensagem.
        // É assim que a mensagem de erro é armazenada na exceção e pode ser
        // recuperada posteriormente (por exemplo, pelo GlobalExceptionHandler
        // usando 'ex.getMessage()').
        super(mensagem);
    }

}
