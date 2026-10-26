package com.biblioteca.repository;

import com.biblioteca.model.Livro;
import com.biblioteca.model.StatusLeitura;
import java.util.List;
import java.util.Optional;

/**
 * Interface de Repositório para o gerenciamento de persistência de Livros.
 * Demonstra os conceitos de Orientação a Objetos:
 * - Abstração: define o contrato das operações de dados sem expor a implementação
 * - Desacoplamento / Inversão de Dependências (DIP do SOLID)
 */
public interface LivroRepository {

    /**
     * Salva ou atualiza um livro no repositório.
     *
     * @param livro Livro a ser salvo
     * @return Livro salvo com identificador gerado (se for novo)
     */
    Livro salvar(Livro livro);

    /**
     * Busca um livro pelo seu identificador único.
     *
     * @param id Identificador do livro
     * @return Optional contendo o livro, se encontrado
     */
    Optional<Livro> buscarPorId(Long id);

    /**
     * Retorna a lista de todos os livros cadastrados.
     *
     * @return Lista de livros
     */
    List<Livro> listarTodos();

    /**
     * Filtra os livros pelo status de leitura.
     *
     * @param status Status desejado
     * @return Lista de livros com o status informado
     */
    List<Livro> buscarPorStatus(StatusLeitura status);

    /**
     * Busca livros por correspondência de texto no título, autor ou gênero.
     *
     * @param termo Palavra-chave para busca
     * @return Lista de livros correspondentes
     */
    List<Livro> buscarPorTermo(String termo);

    /**
     * Verifica se existe um livro cadastrado com o ID especificado.
     *
     * @param id Identificador do livro
     * @return true se existir, false caso contrário
     */
    boolean existePorId(Long id);

    /**
     * Remove um livro do sistema pelo seu identificador.
     *
     * @param id Identificador do livro a remover
     * @return true se o livro foi encontrado e removido, false caso contrário
     */
    boolean excluirPorId(Long id);

    /**
     * Retorna o total de livros cadastrados.
     *
     * @return Quantidade de livros
     */
    long contar();

    /**
     * Remove todos os livros (útil para limpeza entre execuções de testes).
     */
    void limpar();
}
