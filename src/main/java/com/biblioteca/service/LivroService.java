package com.biblioteca.service;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.dto.LivroResponseDTO;
import com.biblioteca.model.StatusLeitura;
import java.util.List;

/**
 * Interface do serviço de gerenciamento de livros.
 * Define o contrato das operações de negócio exigidas pelo trabalho prático:
 * - Cadastrar
 * - Listar
 * - Consultar
 * - Atualizar
 * - Excluir
 */
public interface LivroService {

    /**
     * Cadastra um novo livro no sistema.
     *
     * @param request Dados do livro a ser cadastrado
     * @return DTO com os dados do livro cadastrado, incluindo ID gerado
     */
    LivroResponseDTO cadastrar(LivroRequestDTO request);

    /**
     * Lista todos os livros cadastrados com opções de filtro.
     *
     * @param status Status opcional para filtragem
     * @param termo Termo de busca opcional (título, autor ou gênero)
     * @return Lista de livros cadastrados
     */
    List<LivroResponseDTO> listar(StatusLeitura status, String termo);

    /**
     * Consulta um livro específico pelo seu identificador (ID).
     *
     * @param id Identificador único do livro
     * @return DTO com as informações do livro
     * @throws com.biblioteca.exception.LivroNaoEncontradoException se não existir
     */
    LivroResponseDTO consultarPorId(Long id);

    /**
     * Atualiza as informações de um livro existente.
     *
     * @param id Identificador do livro a ser atualizado
     * @param request Novos dados do livro
     * @return DTO com as informações atualizadas
     * @throws com.biblioteca.exception.LivroNaoEncontradoException se não existir
     */
    LivroResponseDTO atualizar(Long id, LivroRequestDTO request);

    /**
     * Remove um livro do sistema pelo seu identificador.
     *
     * @param id Identificador do livro a remover
     * @throws com.biblioteca.exception.LivroNaoEncontradoException se não existir
     */
    void excluir(Long id);

    /**
     * Retorna a quantidade total de livros cadastrados.
     *
     * @return Total de livros
     */
    long totalLivros();
}
