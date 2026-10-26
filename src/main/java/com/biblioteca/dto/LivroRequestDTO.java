package com.biblioteca.dto;

import com.biblioteca.model.Livro;
import com.biblioteca.model.StatusLeitura;

/**
 * Objeto de Transferência de Dados (DTO) para entrada de informações de Livro.
 * Utilizado nos endpoints de cadastro e atualização (POST e PUT).
 * Isola a camada de apresentação das entidades de domínio internas.
 */
public class LivroRequestDTO {

    private String titulo;
    private String autor;
    private String genero;
    private Integer anoPublicacao;
    private StatusLeitura statusLeitura;
    private String observacoes;

    public LivroRequestDTO() {
    }

    public LivroRequestDTO(String titulo, String autor, String genero, 
                           Integer anoPublicacao, StatusLeitura statusLeitura) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anoPublicacao = anoPublicacao;
        this.statusLeitura = statusLeitura;
    }

    public LivroRequestDTO(String titulo, String autor, String genero, 
                           Integer anoPublicacao, StatusLeitura statusLeitura, String observacoes) {
        this(titulo, autor, genero, anoPublicacao, statusLeitura);
        this.observacoes = observacoes;
    }

    /**
     * Converte este DTO para a entidade de domínio Livro.
     *
     * @return Objeto Livro instanciado
     */
    public Livro paraEntidade() {
        Livro livro = new Livro();
        livro.setTitulo(this.titulo);
        livro.setAutor(this.autor);
        livro.setGenero(this.genero);
        livro.setAnoPublicacao(this.anoPublicacao);
        livro.setStatusLeitura(this.statusLeitura != null ? this.statusLeitura : StatusLeitura.NAO_LIDO);
        livro.setObservacoes(this.observacoes);
        return livro;
    }

    // --- Getters e Setters ---

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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
