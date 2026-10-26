package com.biblioteca.exception;

/**
 * Exceção lançada quando um livro consultado por ID não é encontrado no sistema.
 * Demonstra herança e criação de exceções personalizadas em Java.
 */
public class LivroNaoEncontradoException extends RuntimeException {

    private final Long id;

    public LivroNaoEncontradoException(Long id) {
        super("Livro não encontrado com o identificador: " + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
