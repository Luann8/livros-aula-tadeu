package com.biblioteca.exception;

/**
 * Exceção lançada quando regras de validação ou de negócio dos livros são violadas.
 */
public class ValidacaoException extends RuntimeException {

    public ValidacaoException(String message) {
        super(message);
    }
}
