package com.biblioteca.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários do Enum StatusLeitura (POO)")
class StatusLeituraTest {

    @Test
    @DisplayName("Deve converter nomes de status corretamente")
    void deveConverterPorNome() {
        assertEquals(StatusLeitura.LIDO, StatusLeitura.fromString("LIDO"));
        assertEquals(StatusLeitura.LENDO, StatusLeitura.fromString("lendo"));
        assertEquals(StatusLeitura.QUERO_LER, StatusLeitura.fromString("QUERO_LER"));
        assertEquals(StatusLeitura.QUERO_LER, StatusLeitura.fromString("Quero Ler"));
        assertEquals(StatusLeitura.NAO_LIDO, StatusLeitura.fromString("NAO_LIDO"));
        assertEquals(StatusLeitura.ABANDONADO, StatusLeitura.fromString("Abandonado"));
    }

    @Test
    @DisplayName("Deve retornar null para entrada nula ou vazia")
    void deveRetornarNullParaEntradaVazia() {
        assertNull(StatusLeitura.fromString(null));
        assertNull(StatusLeitura.fromString("   "));
    }

    @Test
    @DisplayName("Deve lançar exceção para status inválido")
    void deveLancarExcecaoParaStatusInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> StatusLeitura.fromString("STATUS_INEXISTENTE"));
        assertTrue(ex.getMessage().contains("Status de leitura inválido"));
    }
}
