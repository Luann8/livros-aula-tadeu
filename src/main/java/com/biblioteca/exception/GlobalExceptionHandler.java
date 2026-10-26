package com.biblioteca.exception;

import com.biblioteca.dto.ErroRespostaDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Interceptador global de exceções para toda a camada de controladores REST.
 * Retorna respostas HTTP semânticas e padronizadas em formato JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LivroNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> handleLivroNaoEncontrado(
            LivroNaoEncontradoException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.NOT_FOUND.value(),
                "Não Encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler({ValidacaoException.class, IllegalArgumentException.class})
    public ResponseEntity<ErroRespostaDTO> handleValidacao(
            RuntimeException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição Inválida",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroRespostaDTO> handleMensagemIlegivel(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        String mensagem = "Corpo da requisição ausente ou em formato inválido.";
        if (ex.getMessage() != null && ex.getMessage().contains("StatusLeitura")) {
            mensagem = "Status de leitura inválido. Valores aceitos: NAO_LIDO, LENDO, LIDO, QUERO_LER, ABANDONADO.";
        }
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                "JSON Inválido",
                mensagem,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroRespostaDTO> handleTipoParametroInvalido(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String mensagem = String.format("O parâmetro '%s' recebeu um valor incompatível: '%s'.", 
                ex.getName(), ex.getValue());
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Parâmetro Incompatível",
                mensagem,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDTO> handleErroInesperado(
            Exception ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                "Ocorreu um erro interno inesperado: " + ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
