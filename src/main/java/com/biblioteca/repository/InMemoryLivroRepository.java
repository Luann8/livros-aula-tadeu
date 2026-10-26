package com.biblioteca.repository;

import com.biblioteca.model.Livro;
import com.biblioteca.model.StatusLeitura;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementação em memória do repositório de livros.
 * Demonstra:
 * - Polimorfismo: implementa a interface LivroRepository
 * - Estruturas de dados em Java (ConcurrentHashMap, AtomicLong, Streams)
 * - Thread-safety para ambientes concorrentes de servidores web
 */
@Repository
public class InMemoryLivroRepository implements LivroRepository {

    private final Map<Long, Livro> dados = new ConcurrentHashMap<>();
    private final AtomicLong sequenciaId = new AtomicLong(1);

    @Override
    public Livro salvar(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("O livro não pode ser nulo para persistência.");
        }

        if (livro.getId() == null) {
            // Novo cadastro: gera um ID sequencial único
            livro.setId(sequenciaId.getAndIncrement());
            if (livro.getDataCadastro() == null) {
                livro.setDataCadastro(LocalDateTime.now());
            }
            livro.setDataAtualizacao(LocalDateTime.now());
        } else {
            // Atualização: atualiza timestamp
            livro.setDataAtualizacao(LocalDateTime.now());
        }

        // Armazena no mapa de dados
        dados.put(livro.getId(), livro);
        return livro;
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Livro> listarTodos() {
        return dados.values().stream()
                .sorted(Comparator.comparing(Livro::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Livro> buscarPorStatus(StatusLeitura status) {
        if (status == null) return listarTodos();
        return dados.values().stream()
                .filter(livro -> status.equals(livro.getStatusLeitura()))
                .sorted(Comparator.comparing(Livro::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Livro> buscarPorTermo(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodos();
        }
        String termoBusca = termo.trim().toLowerCase();

        return dados.values().stream()
                .filter(livro -> 
                    (livro.getTitulo() != null && livro.getTitulo().toLowerCase().contains(termoBusca)) ||
                    (livro.getAutor() != null && livro.getAutor().toLowerCase().contains(termoBusca)) ||
                    (livro.getGenero() != null && livro.getGenero().toLowerCase().contains(termoBusca))
                )
                .sorted(Comparator.comparing(Livro::getId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existePorId(Long id) {
        if (id == null) return false;
        return dados.containsKey(id);
    }

    @Override
    public boolean excluirPorId(Long id) {
        if (id == null) return false;
        return dados.remove(id) != null;
    }

    @Override
    public long contar() {
        return dados.size();
    }

    @Override
    public void limpar() {
        dados.clear();
        sequenciaId.set(1);
    }
}
