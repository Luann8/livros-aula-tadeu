package com.biblioteca.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários da Entidade Livro (POO)")
class LivroTest {

    @Test
    @DisplayName("Deve instanciar um livro válido com construtor parametrizado")
    void deveInstanciarLivroValido() {
        Livro livro = new Livro("O Alquimista", "Paulo Coelho", "Ficção Filosófica", 1988, StatusLeitura.LIDO);

        assertEquals("O Alquimista", livro.getTitulo());
        assertEquals("Paulo Coelho", livro.getAutor());
        assertEquals("Ficção Filosófica", livro.getGenero());
        assertEquals(1988, livro.getAnoPublicacao());
        assertEquals(StatusLeitura.LIDO, livro.getStatusLeitura());
        assertNotNull(livro.getDataCadastro());
        assertNotNull(livro.getDataAtualizacao());
        assertDoesNotThrow(livro::validar);
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar livro sem título")
    void deveLancarExcecaoSemTitulo() {
        Livro livro = new Livro("   ", "Paulo Coelho", "Ficção", 1988, StatusLeitura.LIDO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, livro::validar);
        assertTrue(ex.getMessage().contains("título do livro é obrigatório"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar livro sem autor")
    void deveLancarExcecaoSemAutor() {
        Livro livro = new Livro("O Alquimista", "", "Ficção", 1988, StatusLeitura.LIDO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, livro::validar);
        assertTrue(ex.getMessage().contains("autor do livro é obrigatório"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar livro sem gênero")
    void deveLancarExcecaoSemGenero() {
        Livro livro = new Livro("O Alquimista", "Paulo Coelho", null, 1988, StatusLeitura.LIDO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, livro::validar);
        assertTrue(ex.getMessage().contains("gênero do livro é obrigatório"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar livro com ano inválido")
    void deveLancarExcecaoComAnoInvalido() {
        int anoFuturo = Year.now().getValue() + 5;
        Livro livroFuturo = new Livro("Livro do Futuro", "Autor", "Ficção", anoFuturo, StatusLeitura.QUERO_LER);
        assertThrows(IllegalArgumentException.class, livroFuturo::validar);

        Livro livroAntigoDemais = new Livro("Livro Antigo", "Autor", "História", 500, StatusLeitura.LIDO);
        assertThrows(IllegalArgumentException.class, livroAntigoDemais::validar);
    }

    @Test
    @DisplayName("Deve atualizar os dados corretamente preservando o ID original")
    void deveAtualizarDadosCorretamente() {
        Livro original = new Livro(10L, "Título Antigo", "Autor Antigo", "Gênero Antigo", 2000, 
                StatusLeitura.NAO_LIDO, "Nota antiga");

        Livro novosDados = new Livro("Título Novo", "Autor Novo", "Gênero Novo", 2024, 
                StatusLeitura.LENDO);
        novosDados.setObservacoes("Nota nova");

        original.atualizarCom(novosDados);

        assertEquals(10L, original.getId());
        assertEquals("Título Novo", original.getTitulo());
        assertEquals("Autor Novo", original.getAutor());
        assertEquals("Gênero Novo", original.getGenero());
        assertEquals(2024, original.getAnoPublicacao());
        assertEquals(StatusLeitura.LENDO, original.getStatusLeitura());
        assertEquals("Nota nova", original.getObservacoes());
    }

    @Test
    @DisplayName("Deve validar igualdade baseada no ID (equals e hashCode)")
    void deveValidarEqualsEHashCodePorId() {
        Livro livro1 = new Livro();
        livro1.setId(1L);
        livro1.setTitulo("A");

        Livro livro2 = new Livro();
        livro2.setId(1L);
        livro2.setTitulo("B");

        Livro livro3 = new Livro();
        livro3.setId(2L);

        assertEquals(livro1, livro2);
        assertEquals(livro1.hashCode(), livro2.hashCode());
        assertNotEquals(livro1, livro3);
    }
}
