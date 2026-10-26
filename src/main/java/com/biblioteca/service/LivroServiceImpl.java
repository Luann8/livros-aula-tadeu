package com.biblioteca.service;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.dto.LivroResponseDTO;
import com.biblioteca.exception.LivroNaoEncontradoException;
import com.biblioteca.exception.ValidacaoException;
import com.biblioteca.model.Livro;
import com.biblioteca.model.StatusLeitura;
import com.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação da regra de negócio de gerenciamento de livros.
 * Demonstra:
 * - Injeção de dependência pelo construtor (Inversão de Controle do Spring)
 * - Validações de integridade e negócio
 * - Tratamento de exceções específicas da aplicação
 */
@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;

    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public LivroResponseDTO cadastrar(LivroRequestDTO request) {
        if (request == null) {
            throw new ValidacaoException("Os dados do livro não podem ser nulos.");
        }

        // Converte o DTO em objeto de domínio Livro (POO)
        Livro livro = request.paraEntidade();

        // Executa validações de regras de negócio encapsuladas na entidade
        try {
            livro.validar();
        } catch (IllegalArgumentException e) {
            throw new ValidacaoException(e.getMessage());
        }

        // Persiste o livro no repositório
        Livro livroSalvo = livroRepository.salvar(livro);
        return LivroResponseDTO.aPartirDe(livroSalvo);
    }

    @Override
    public List<LivroResponseDTO> listar(StatusLeitura status, String termo) {
        List<Livro> livros;

        if (status != null) {
            livros = livroRepository.buscarPorStatus(status);
            if (termo != null && !termo.trim().isEmpty()) {
                String termoLower = termo.trim().toLowerCase();
                livros = livros.stream()
                        .filter(l -> (l.getTitulo() != null && l.getTitulo().toLowerCase().contains(termoLower)) ||
                                     (l.getAutor() != null && l.getAutor().toLowerCase().contains(termoLower)) ||
                                     (l.getGenero() != null && l.getGenero().toLowerCase().contains(termoLower)))
                        .collect(Collectors.toList());
            }
        } else if (termo != null && !termo.trim().isEmpty()) {
            livros = livroRepository.buscarPorTermo(termo);
        } else {
            livros = livroRepository.listarTodos();
        }

        return livros.stream()
                .map(LivroResponseDTO::aPartirDe)
                .collect(Collectors.toList());
    }

    @Override
    public LivroResponseDTO consultarPorId(Long id) {
        if (id == null) {
            throw new ValidacaoException("O identificador do livro é obrigatório para consulta.");
        }

        Livro livro = livroRepository.buscarPorId(id)
                .orElseThrow(() -> new LivroNaoEncontradoException(id));

        return LivroResponseDTO.aPartirDe(livro);
    }

    @Override
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO request) {
        if (id == null) {
            throw new ValidacaoException("O identificador do livro é obrigatório para atualização.");
        }
        if (request == null) {
            throw new ValidacaoException("Os novos dados do livro não podem ser nulos.");
        }

        // Busca o livro existente
        Livro livroExistente = livroRepository.buscarPorId(id)
                .orElseThrow(() -> new LivroNaoEncontradoException(id));

        // Cria a entidade com os novos dados
        Livro novosDados = request.paraEntidade();

        // Atualiza os dados através do método de domínio da entidade (POO)
        try {
            livroExistente.atualizarCom(novosDados);
        } catch (IllegalArgumentException e) {
            throw new ValidacaoException(e.getMessage());
        }

        // Salva a entidade atualizada
        Livro livroAtualizado = livroRepository.salvar(livroExistente);
        return LivroResponseDTO.aPartirDe(livroAtualizado);
    }

    @Override
    public void excluir(Long id) {
        if (id == null) {
            throw new ValidacaoException("O identificador do livro é obrigatório para exclusão.");
        }

        if (!livroRepository.existePorId(id)) {
            throw new LivroNaoEncontradoException(id);
        }

        livroRepository.excluirPorId(id);
    }

    @Override
    public long totalLivros() {
        return livroRepository.contar();
    }
}
