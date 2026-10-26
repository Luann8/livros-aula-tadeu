package com.biblioteca.dto;

import com.biblioteca.model.Livro;
import com.biblioteca.model.StatusLeitura;
import java.time.LocalDateTime;

/**
 * DTO que padroniza a saída de dados de um Livro nas respostas HTTP.
 */
public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
    private String genero;
    private Integer anoPublicacao;
    private StatusLeitura statusLeitura;
    private String statusDescricao;
    private String observacoes;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public LivroResponseDTO() {
    }

    public static LivroResponseDTO aPartirDe(Livro livro) {
        if (livro == null) return null;

        LivroResponseDTO dto = new LivroResponseDTO();
        dto.setId(livro.getId());
        dto.setTitulo(livro.getTitulo());
        dto.setAutor(livro.getAutor());
        dto.setGenero(livro.getGenero());
        dto.setAnoPublicacao(livro.getAnoPublicacao());
        dto.setStatusLeitura(livro.getStatusLeitura());
        dto.setStatusDescricao(livro.getStatusLeitura() != null ? livro.getStatusLeitura().getDescricao() : "N/A");
        dto.setObservacoes(livro.getObservacoes());
        dto.setDataCadastro(livro.getDataCadastro());
        dto.setDataAtualizacao(livro.getDataAtualizacao());
        return dto;
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public StatusLeitura getStatusLeitura() {
        return statusLeitura;
    }

    public void setStatusLeitura(StatusLeitura statusLeitura) {
        this.statusLeitura = statusLeitura;
    }

    public String getStatusDescricao() {
        return statusDescricao;
    }

    public void setStatusDescricao(String statusDescricao) {
        this.statusDescricao = statusDescricao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
