package com.danilo.crud_angular_spring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.danilo.crud_angular_spring.exceptions.RecursoNaoEncontradoException;
import com.danilo.crud_angular_spring.model.Produto;
import com.danilo.crud_angular_spring.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Produto com ID "+id+" nao encontrado."));
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void deletarProduto(Long id) {

        if (!produtoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto com ID "+id+" nao encontrado.");
        }
        produtoRepository.deleteById(id);
    }

}
