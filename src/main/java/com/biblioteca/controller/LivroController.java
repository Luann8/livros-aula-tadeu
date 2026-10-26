package com.biblioteca.controller;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.dto.LivroResponseDTO;
import com.biblioteca.model.StatusLeitura;
import com.biblioteca.service.LivroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST que expõe os endpoints para gerenciar os livros da biblioteca pessoal.
 * Implementa as funcionalidades exigidas:
 * - Cadastrar (POST /api/livros)
 * - Listar (GET /api/livros)
 * - Consultar (GET /api/livros/{id})
 * - Atualizar (PUT /api/livros/{id})
 * - Excluir (DELETE /api/livros/{id})
 */
@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = "*")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    /**
     * Cadastrar: adiciona um novo livro ao sistema.
     *
     * @param request Dados do livro a ser cadastrado
     * @return LivroResponseDTO com código HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<LivroResponseDTO> cadastrar(@RequestBody LivroRequestDTO request) {
        LivroResponseDTO livroCadastrado = livroService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroCadastrado);
    }

    /**
     * Listar: exibe todos os livros cadastrados, com suporte a filtros por status e busca textual.
     *
     * @param status Filtro opcional por status de leitura
     * @param busca Filtro opcional por termo de busca no título, autor ou gênero
     * @return Lista de livros cadastrados com código HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listar(
            @RequestParam(required = false) StatusLeitura status,
            @RequestParam(required = false) String busca) {
        List<LivroResponseDTO> livros = livroService.listar(status, busca);
        return ResponseEntity.ok(livros);
    }

    /**
     * Consultar: busca um livro específico pelo seu identificador.
     *
     * @param id Identificador único do livro
     * @return LivroResponseDTO com código HTTP 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> consultarPorId(@PathVariable Long id) {
        LivroResponseDTO livro = livroService.consultarPorId(id);
        return ResponseEntity.ok(livro);
    }

    /**
     * Atualizar: modifica as informações de um livro cadastrado.
     *
     * @param id Identificador do livro a ser modificado
     * @param request Novos dados do livro
     * @return LivroResponseDTO atualizado com código HTTP 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody LivroRequestDTO request) {
        LivroResponseDTO livroAtualizado = livroService.atualizar(id, request);
        return ResponseEntity.ok(livroAtualizado);
    }

    /**
     * Excluir: remove um livro do sistema pelo seu identificador.
     *
     * @param id Identificador do livro a remover
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        livroService.excluir(id);
    }

    /**
     * Endpoint utilitário para listar todos os status de leitura disponíveis.
     * Facilita a integração com interfaces web e formulários.
     *
     * @return Lista de objetos com o nome do enum e sua descrição
     */
    @GetMapping("/status")
    public ResponseEntity<List<Map<String, String>>> listarStatusDisponiveis() {
        List<Map<String, String>> statusList = Arrays.stream(StatusLeitura.values())
                .map(s -> {
                    Map<String, String> item = new HashMap<>();
                    item.put("codigo", s.name());
                    item.put("descricao", s.getDescricao());
                    return item;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(statusList);
    }

    /**
     * Retorna a quantidade total de livros cadastrados.
     *
     * @return Mapa com o total
     */
    @GetMapping("/total")
    public ResponseEntity<Map<String, Long>> totalLivros() {
        Map<String, Long> resposta = new HashMap<>();
        resposta.put("total", livroService.totalLivros());
        return ResponseEntity.ok(resposta);
    }
}
