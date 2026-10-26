package com.biblioteca.controller;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.dto.LivroResponseDTO;
import com.biblioteca.exception.GlobalExceptionHandler;
import com.biblioteca.exception.LivroNaoEncontradoException;
import com.biblioteca.exception.ValidacaoException;
import com.biblioteca.model.StatusLeitura;
import com.biblioteca.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivroController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("Testes da API REST (LivroController)")
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LivroService livroService;

    @Test
    @DisplayName("POST /api/livros - Deve cadastrar livro com sucesso e retornar 201 Created")
    void deveCadastrarLivroRetornando201() throws Exception {
        LivroRequestDTO request = new LivroRequestDTO(
                "O Código Da Vinci", "Dan Brown", "Suspense", 2003, StatusLeitura.LIDO
        );

        LivroResponseDTO response = new LivroResponseDTO();
        response.setId(1L);
        response.setTitulo("O Código Da Vinci");
        response.setAutor("Dan Brown");
        response.setGenero("Suspense");
        response.setAnoPublicacao(2003);
        response.setStatusLeitura(StatusLeitura.LIDO);
        response.setStatusDescricao("Lido");

        when(livroService.cadastrar(any(LivroRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("O Código Da Vinci"))
                .andExpect(jsonPath("$.autor").value("Dan Brown"));
    }

    @Test
    @DisplayName("POST /api/livros - Deve retornar 400 Bad Request quando os dados forem inválidos")
    void deveRetornar400AoCadastrarDadosInvalidos() throws Exception {
        when(livroService.cadastrar(any(LivroRequestDTO.class)))
                .thenThrow(new ValidacaoException("O título do livro é obrigatório."));

        LivroRequestDTO requestInvalido = new LivroRequestDTO();

        mockMvc.perform(post("/api/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem").value("O título do livro é obrigatório."));
    }

    @Test
    @DisplayName("GET /api/livros - Deve listar todos os livros e retornar 200 OK")
    void deveListarLivrosRetornando200() throws Exception {
        LivroResponseDTO livro = new LivroResponseDTO();
        livro.setId(1L);
        livro.setTitulo("Duna");
        livro.setAutor("Frank Herbert");
        livro.setGenero("Ficção Científica");
        livro.setAnoPublicacao(1965);
        livro.setStatusLeitura(StatusLeitura.LENDO);

        when(livroService.listar(null, null)).thenReturn(List.of(livro));

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Duna"));
    }

    @Test
    @DisplayName("GET /api/livros/{id} - Deve consultar livro por ID e retornar 200 OK")
    void deveConsultarPorIdRetornando200() throws Exception {
        LivroResponseDTO livro = new LivroResponseDTO();
        livro.setId(5L);
        livro.setTitulo("Fahrenheit 451");

        when(livroService.consultarPorId(5L)).thenReturn(livro);

        mockMvc.perform(get("/api/livros/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.titulo").value("Fahrenheit 451"));
    }

    @Test
    @DisplayName("GET /api/livros/{id} - Deve retornar 404 Not Found quando ID não existir")
    void deveRetornar404AoConsultarIdInexistente() throws Exception {
        when(livroService.consultarPorId(999L))
                .thenThrow(new LivroNaoEncontradoException(999L));

        mockMvc.perform(get("/api/livros/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Livro não encontrado com o identificador: 999"));
    }

    @Test
    @DisplayName("PUT /api/livros/{id} - Deve atualizar livro e retornar 200 OK")
    void deveAtualizarLivroRetornando200() throws Exception {
        LivroRequestDTO request = new LivroRequestDTO(
                "Duna: Messias", "Frank Herbert", "Ficção Científica", 1969, StatusLeitura.LIDO
        );

        LivroResponseDTO atualizado = new LivroResponseDTO();
        atualizado.setId(1L);
        atualizado.setTitulo("Duna: Messias");
        atualizado.setStatusLeitura(StatusLeitura.LIDO);

        when(livroService.atualizar(eq(1L), any(LivroRequestDTO.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/livros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Duna: Messias"));
    }

    @Test
    @DisplayName("DELETE /api/livros/{id} - Deve excluir livro e retornar 204 No Content")
    void deveExcluirLivroRetornando204() throws Exception {
        doNothing().when(livroService.excluir(1L));

        mockMvc.perform(delete("/api/livros/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/livros/{id} - Deve retornar 404 Not Found ao excluir ID inexistente")
    void deveRetornar404AoExcluirIdInexistente() throws Exception {
        doThrow(new LivroNaoEncontradoException(999L)).when(livroService.excluir(999L));

        mockMvc.perform(delete("/api/livros/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
