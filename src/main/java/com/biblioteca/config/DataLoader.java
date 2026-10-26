package com.biblioteca.config;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.model.StatusLeitura;
import com.biblioteca.service.LivroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Carga inicial de dados para demonstração da biblioteca pessoal.
 * Popula a aplicação com exemplos práticos de livros logo após a inicialização.
 */
@Configuration
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final LivroService livroService;

    public DataLoader(LivroService livroService) {
        this.livroService = livroService;
    }

    @Override
    public void run(String... args) {
        logger.info("Carregando livros iniciais para demonstração...");

        livroService.cadastrar(new LivroRequestDTO(
                "Dom Casmurro",
                "Machado de Assis",
                "Romance / Clássico",
                1899,
                StatusLeitura.LIDO,
                "Edição comemorativa com notas explicativas sobre Capitu."
        ));

        livroService.cadastrar(new LivroRequestDTO(
                "O Senhor dos Anéis: A Sociedade do Anel",
                "J.R.R. Tolkien",
                "Alta Fantasia",
                1954,
                StatusLeitura.LENDO,
                "Lendo o capítulo sobre Valfenda. Fantástica construção de mundo."
        ));

        livroService.cadastrar(new LivroRequestDTO(
                "Clean Code: A Handbook of Agile Software Craftsmanship",
                "Robert C. Martin",
                "Técnico / Computação",
                2008,
                StatusLeitura.QUERO_LER,
                "Recomendado para aprimorar técnicas de POO e Clean Code."
        ));

        livroService.cadastrar(new LivroRequestDTO(
                "1984",
                "George Orwell",
                "Ficção Distópica",
                1949,
                StatusLeitura.LIDO,
                "Clássico atemporal sobre vigilância e autoritarismo."
        ));

        logger.info("Carga inicial concluída com sucesso! Total de livros cadastrados: {}", livroService.totalLivros());
    }
}
