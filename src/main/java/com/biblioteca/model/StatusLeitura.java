package com.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum que representa o status de leitura de um livro.
 * Demonstra conceitos de Orientação a Objetos como enumerações ricas com atributos,
 * construtores privados, métodos e encapsulamento.
 */
public enum StatusLeitura {
    NAO_LIDO("Não Lido"),
    LENDO("Lendo"),
    LIDO("Lido"),
    QUERO_LER("Quero Ler"),
    ABANDONADO("Abandonado");

    private final String descricao;

    StatusLeitura(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    /**
     * Converte uma String (nome ou descrição) para o enum correspondente de forma flexível.
     *
     * @param valor Nome ou descrição do status
     * @return StatusLeitura correspondente
     * @throws IllegalArgumentException se o valor não corresponder a nenhum status
     */
    @JsonCreator
    public static StatusLeitura fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        String valorNormalizado = valor.trim().replace(" ", "_").toUpperCase();

        for (StatusLeitura status : StatusLeitura.values()) {
            if (status.name().equalsIgnoreCase(valorNormalizado) ||
                status.descricao.equalsIgnoreCase(valor.trim())) {
                return status;
            }
        }

        // Tenta equivalências comuns
        if (valorNormalizado.contains("NAO") || valorNormalizado.contains("NÃO")) {
            return NAO_LIDO;
        } else if (valorNormalizado.contains("QUERO")) {
            return QUERO_LER;
        }

        throw new IllegalArgumentException("Status de leitura inválido: '" + valor + 
                "'. Valores aceitos: NAO_LIDO, LENDO, LIDO, QUERO_LER, ABANDONADO");
    }

    @Override
    public String toString() {
        return descricao;
    }
}
