package com.danilo.crud_angular_spring.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danilo.crud_angular_spring.model.Usuario;
import com.danilo.crud_angular_spring.security.JwtUtil;
import com.danilo.crud_angular_spring.service.UsuarioService;

// Define que esta classe é um controlador REST, que lida com requisições HTTP
@RestController
// Define o caminho base para todas as requisições que chegam a este controlador
@RequestMapping("/auth")
public class AuthController {

    // Injeta o serviço que lida com a lógica dos usuários
    private final UsuarioService usuarioService;

    // Construtor que recebe o serviço de usuário via injeção de dependência
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para registrar um novo usuário.
     * Espera um corpo da requisição no formato JSON com a chave "username".
     * Cria o usuário com a senha fixa "password" (isso deve ser ajustado futuramente para vir da requisição e ser armazenado de forma segura).
     * Retorna o usuário criado como resposta.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        Usuario usuario = usuarioService.registrarUsuario(request.get("username"), "password");
        return ResponseEntity.ok(usuario); // Retorna o usuário criado com status 200 OK
    }

    /**
     * Endpoint de login.
     * Espera um JSON com as chaves "username" e "password".
     * Verifica se o usuário existe e se a senha fornecida é igual à cadastrada.
     * Se estiver correto, gera um token JWT e o retorna ao cliente.
     * Se estiver incorreto, retorna status 401 (não autorizado).
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        Optional<Usuario> usuario = usuarioService.buscarPorUsername(request.get("username"));

        // Se o usuário existir e a senha bater, gera token JWT
        if (usuario.isPresent() && usuario.get().getPassword().equals(request.get("password"))) {
            String token = JwtUtil.generateToken(usuario.get().getUsername());
            return ResponseEntity.ok(Map.of("token", token)); // Retorna o token JWT
        }

        // Senha incorreta ou usuário não encontrado
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

}

