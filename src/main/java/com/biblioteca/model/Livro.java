package com.biblioteca.model;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Objects;

/**
 * Classe de modelo que representa um Livro na biblioteca pessoal.
 * Implementa conceitos fundamentais de Programação Orientada a Objetos:
 * - Encapsulamento: atributos privados com métodos acessores (getters e setters)
 * - Construtores sobrecarregados (sobrecarga de métodos)
 * - Métodos de domínio para validação e atualização de estado
 * - Sobrescrita de métodos herdados da classe Object (equals, hashCode, toString)
 */
public class Livro {

    private Long id;
    private String titulo;
    private String autor;
    private String genero;
    private Integer anoPublicacao;
    private StatusLeitura statusLeitura;
    private String observacoes;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    /**
     * Construtor padrão (sem argumentos).
     * Necessário para serialização/deserialização e instanciação reflexiva.
     */
    public Livro() {
        this.dataCadastro = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        this.statusLeitura = StatusLeitura.NAO_LIDO;
    }

    /**
     * Construtor com os atributos essenciais solicitados pelo trabalho.
     *
     * @param titulo Título da obra
     * @param autor Autor da obra
     * @param genero Gênero literário
     * @param anoPublicacao Ano em que a obra foi publicada
     * @param statusLeitura Status atual de leitura da obra
     */
    public Livro(String titulo, String autor, String genero, Integer anoPublicacao, StatusLeitura statusLeitura) {
        this();
        this.titulo = titulo != null ? titulo.trim() : null;
        this.autor = autor != null ? autor.trim() : null;
        this.genero = genero != null ? genero.trim() : null;
        this.anoPublicacao = anoPublicacao;
        this.statusLeitura = statusLeitura != null ? statusLeitura : StatusLeitura.NAO_LIDO;
    }

    /**
     * Construtor completo com todos os campos incluindo observações.
     *
     * @param id Identificador do livro
     * @param titulo Título da obra
     * @param autor Autor da obra
     * @param genero Gênero literário
     * @param anoPublicacao Ano de publicação
     * @param statusLeitura Status de leitura
     * @param observacoes Notas e anotações pessoais do leitor
     */
    public Livro(Long id, String titulo, String autor, String genero, Integer anoPublicacao, 
                 StatusLeitura statusLeitura, String observacoes) {
        this(titulo, autor, genero, anoPublicacao, statusLeitura);
        this.id = id;
        this.observacoes = observacoes != null ? observacoes.trim() : "";
    }

    /**
     * Valida as regras de negócio e integridade do Livro.
     * Garante que os atributos obrigatórios estejam preenchidos e válidos.
     *
     * @throws IllegalArgumentException se algum dado for inválido
     */
    public void validar() {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título do livro é obrigatório.");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("O autor do livro é obrigatório.");
        }
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("O gênero do livro é obrigatório.");
        }
        if (anoPublicacao == null) {
            throw new IllegalArgumentException("O ano de publicação é obrigatório.");
        }
        int anoAtual = Year.now().getValue();
        if (anoPublicacao < 1000 || anoPublicacao > anoAtual + 1) {
            throw new IllegalArgumentException("O ano de publicação deve ser um ano válido entre 1000 e " + (anoAtual + 1) + ".");
        }
        if (statusLeitura == null) {
            throw new IllegalArgumentException("O status de leitura é obrigatório.");
        }
    }

    /**
     * Atualiza os dados deste livro a partir de outro objeto Livro.
     * Preserva o ID original e a data de cadastro, atualizando a data de modificação.
     *
     * @param novosDados Livro contendo as novas informações
     */
    public void atualizarCom(Livro novosDados) {
        if (novosDados == null) {
            throw new IllegalArgumentException("Os dados para atualização não podem ser nulos.");
        }
        novosDados.validar();

        this.titulo = novosDados.getTitulo().trim();
        this.autor = novosDados.getAutor().trim();
        this.genero = novosDados.getGenero().trim();
        this.anoPublicacao = novosDados.getAnoPublicacao();
        this.statusLeitura = novosDados.getStatusLeitura();
        if (novosDados.getObservacoes() != null) {
            this.observacoes = novosDados.getObservacoes().trim();
        }
        this.dataAtualizacao = LocalDateTime.now();
    }

    // --- Getters e Setters (Encapsulamento) ---

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
        this.titulo = titulo != null ? titulo.trim() : null;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor != null ? autor.trim() : null;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero != null ? genero.trim() : null;
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
        this.observacoes = observacoes != null ? observacoes.trim() : null;
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

    // --- Sobrescrita dos métodos da classe Object ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", genero='" + genero + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                ", statusLeitura=" + (statusLeitura != null ? statusLeitura.getDescricao() : "N/A") +
                ", observacoes='" + observacoes + '\'' +
                '}';
    }
}
