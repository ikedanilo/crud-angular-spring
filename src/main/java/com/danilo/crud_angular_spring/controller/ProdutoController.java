package com.danilo.crud_angular_spring.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danilo.crud_angular_spring.model.Produto;
import com.danilo.crud_angular_spring.service.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// Define que esta classe é um controlador REST
@RestController
// Define o caminho base para todas as requisições: /api/produtos
@RequestMapping("/api/produtos")
// Lombok: cria automaticamente o construtor com todos os argumentos (injetando ProdutoService)
@AllArgsConstructor
public class ProdutoController {

    // Injeta o serviço que lida com as regras de negócio da entidade Produto
    private final ProdutoService produtoService;

    /**
     * Método GET para listar todos os produtos.
     * Endpoint: GET /api/produtos
     * Retorna uma lista de todos os produtos cadastrados.
     */
    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    /**
     * Método GET para buscar um produto pelo ID.
     * Endpoint: GET /api/produtos/{id}
     * Retorna o produto correspondente ao ID informado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProduto(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto); // Retorna 200 OK com o produto encontrado
    }

    /**
     * Método POST para criar um novo produto.
     * Endpoint: POST /api/produtos
     * Recebe um objeto Produto no corpo da requisição e o salva no banco.
     * Retorna o produto criado.
     */
    @PostMapping
    public Produto criarProduto(@RequestBody Produto produto) {
        return produtoService.salvarProduto(produto);
    }

    /**
     * Método DELETE para remover um produto pelo ID.
     * Endpoint: DELETE /api/produtos/{id}
     * Remove o produto correspondente ao ID informado.
     * Retorna 204 No Content após remoção.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build(); // Retorna 204 indicando sucesso sem corpo
    }

}
