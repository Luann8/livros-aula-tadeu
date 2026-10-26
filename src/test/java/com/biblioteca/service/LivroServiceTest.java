package com.biblioteca.service;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.dto.LivroResponseDTO;
import com.biblioteca.exception.LivroNaoEncontradoException;
import com.biblioteca.exception.ValidacaoException;
import com.biblioteca.model.StatusLeitura;
import com.biblioteca.repository.InMemoryLivroRepository;
import com.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários da Camada de Serviço (LivroService)")
class LivroServiceTest {

    private LivroRepository livroRepository;
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroRepository = new InMemoryLivroRepository();
        livroService = new LivroServiceImpl(livroRepository);
    }

    @Test
    @DisplayName("Cadastrar: Deve cadastrar um novo livro com sucesso gerando ID")
    void deveCadastrarLivroComSucesso() {
        LivroRequestDTO request = new LivroRequestDTO(
                "Memórias Póstumas de Brás Cubas",
                "Machado de Assis",
                "Romance",
                1881,
                StatusLeitura.LIDO,
                "Narrado por um defunto autor."
        );

        LivroResponseDTO response = livroService.cadastrar(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Memórias Póstumas de Brás Cubas", response.getTitulo());
        assertEquals("Machado de Assis", response.getAutor());
        assertEquals(StatusLeitura.LIDO, response.getStatusLeitura());
        assertEquals(1, livroService.totalLivros());
    }

    @Test
    @DisplayName("Cadastrar: Deve lançar ValidacaoException quando os dados forem inválidos")
    void deveLancarExcecaoAoCadastrarDadosInvalidos() {
        LivroRequestDTO requestSemTitulo = new LivroRequestDTO(
                "",
                "Machado de Assis",
                "Romance",
                1881,
                StatusLeitura.LIDO
        );

        assertThrows(ValidacaoException.class, () -> livroService.cadastrar(requestSemTitulo));
        assertThrows(ValidacaoException.class, () -> livroService.cadastrar(null));
    }

    @Test
    @DisplayName("Listar: Deve listar todos os livros cadastrados")
    void deveListarTodosOsLivros() {
        livroService.cadastrar(new LivroRequestDTO("Livro 1", "Autor 1", "Ficção", 2020, StatusLeitura.LIDO));
        livroService.cadastrar(new LivroRequestDTO("Livro 2", "Autor 2", "Romance", 2021, StatusLeitura.LENDO));

        List<LivroResponseDTO> lista = livroService.listar(null, null);

        assertEquals(2, lista.size());
        assertEquals("Livro 1", lista.get(0).getTitulo());
        assertEquals("Livro 2", lista.get(1).getTitulo());
    }

    @Test
    @DisplayName("Listar: Deve filtrar livros por status de leitura")
    void deveFiltrarLivrosPorStatus() {
        livroService.cadastrar(new LivroRequestDTO("Livro A", "Autor A", "Gênero A", 2020, StatusLeitura.LIDO));
        livroService.cadastrar(new LivroRequestDTO("Livro B", "Autor B", "Gênero B", 2021, StatusLeitura.LENDO));
        livroService.cadastrar(new LivroRequestDTO("Livro C", "Autor C", "Gênero C", 2022, StatusLeitura.LIDO));

        List<LivroResponseDTO> lidos = livroService.listar(StatusLeitura.LIDO, null);
        assertEquals(2, lidos.size());

        List<LivroResponseDTO> lendo = livroService.listar(StatusLeitura.LENDO, null);
        assertEquals(1, lendo.size());
        assertEquals("Livro B", lendo.get(0).getTitulo());
    }

    @Test
    @DisplayName("Listar: Deve buscar livros por termo (título, autor ou gênero)")
    void deveBuscarLivrosPorTermo() {
        livroService.cadastrar(new LivroRequestDTO("Clean Architecture", "Robert Martin", "Técnico", 2017, StatusLeitura.LIDO));
        livroService.cadastrar(new LivroRequestDTO("O Hobbit", "J.R.R. Tolkien", "Fantasia", 1937, StatusLeitura.LIDO));

        List<LivroResponseDTO> buscaTolkien = livroService.listar(null, "tolkien");
        assertEquals(1, buscaTolkien.size());
        assertEquals("O Hobbit", buscaTolkien.get(0).getTitulo());

        List<LivroResponseDTO> buscaTecnico = livroService.listar(null, "técnico");
        assertEquals(1, buscaTecnico.size());
        assertEquals("Clean Architecture", buscaTecnico.get(0).getTitulo());
    }

    @Test
    @DisplayName("Consultar: Deve buscar livro por ID existente")
    void deveBuscarLivroPorIdExistente() {
        LivroResponseDTO cadastrado = livroService.cadastrar(
                new LivroRequestDTO("Ensaio sobre a Cegueira", "José Saramago", "Ficção", 1995, StatusLeitura.LIDO)
        );

        LivroResponseDTO encontrado = livroService.consultarPorId(cadastrado.getId());

        assertNotNull(encontrado);
        assertEquals(cadastrado.getId(), encontrado.getId());
        assertEquals("Ensaio sobre a Cegueira", encontrado.getTitulo());
    }

    @Test
    @DisplayName("Consultar: Deve lançar LivroNaoEncontradoException para ID inexistente")
    void deveLancarExcecaoAoBuscarIdInexistente() {
        assertThrows(LivroNaoEncontradoException.class, () -> livroService.consultarPorId(9999L));
        assertThrows(ValidacaoException.class, () -> livroService.consultarPorId(null));
    }

    @Test
    @DisplayName("Atualizar: Deve atualizar os dados de um livro cadastrado com sucesso")
    void deveAtualizarLivroComSucesso() {
        LivroResponseDTO original = livroService.cadastrar(
                new LivroRequestDTO("Título Velho", "Autor Velho", "Gênero", 2020, StatusLeitura.NAO_LIDO)
        );

        LivroRequestDTO novosDados = new LivroRequestDTO(
                "Título Atualizado",
                "Autor Velho",
                "Gênero",
                2020,
                StatusLeitura.LIDO,
                "Livro concluído com sucesso."
        );

        LivroResponseDTO atualizado = livroService.atualizar(original.getId(), novosDados);

        assertEquals(original.getId(), atualizado.getId());
        assertEquals("Título Atualizado", atualizado.getTitulo());
        assertEquals(StatusLeitura.LIDO, atualizado.getStatusLeitura());
        assertEquals("Livro concluído com sucesso.", atualizado.getObservacoes());
    }

    @Test
    @DisplayName("Atualizar: Deve lançar LivroNaoEncontradoException ao atualizar ID inexistente")
    void deveLancarExcecaoAoAtualizarIdInexistente() {
        LivroRequestDTO dados = new LivroRequestDTO("A", "B", "C", 2020, StatusLeitura.LIDO);
        assertThrows(LivroNaoEncontradoException.class, () -> livroService.atualizar(9999L, dados));
    }

    @Test
    @DisplayName("Excluir: Deve remover um livro do sistema com sucesso")
    void deveExcluirLivroComSucesso() {
        LivroResponseDTO livro = livroService.cadastrar(
                new LivroRequestDTO("Livro para Exclusão", "Autor", "Gênero", 2020, StatusLeitura.LIDO)
        );

        assertEquals(1, livroService.totalLivros());

        livroService.excluir(livro.getId());

        assertEquals(0, livroService.totalLivros());
        assertThrows(LivroNaoEncontradoException.class, () -> livroService.consultarPorId(livro.getId()));
    }

    @Test
    @DisplayName("Excluir: Deve lançar LivroNaoEncontradoException ao tentar excluir ID inexistente")
    void deveLancarExcecaoAoExcluirIdInexistente() {
        assertThrows(LivroNaoEncontradoException.class, () -> livroService.excluir(9999L));
    }
}
