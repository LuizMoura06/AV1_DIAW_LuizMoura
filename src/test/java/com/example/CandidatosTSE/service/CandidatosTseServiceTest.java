package com.example.CandidatosTSE.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.example.CandidatosTSE.model.Candidato;

class CandidatosTseServiceTest {

    private static CandidatosTseService service;

    @BeforeAll
    static void carregarBaseFornecida() {
        service = new CandidatosTseService();
        service.carregarCsv();
    }

    @Test
    void carregaOs1832RegistrosDaBase() {
        assertEquals(1832, service.listarTodos().size());
        assertTrue(service.listarTodos().stream().allMatch(c -> "MG".equals(c.getUf())));
    }

    @Test
    void reproduzAsContagensDosFiltrosDeReferencia() {
        assertAll(
                () -> assertEquals(11, service.filtrar("GOVERNADOR", null, null).size()),
                () -> assertEquals(18, service.filtrar("SENADOR", null, null).size()));
    }

    @Test
    void ignoraFiltrosAusentesOuEmBranco() {
        assertAll(
                () -> assertEquals(1832, service.filtrar(null, null, null).size()),
                () -> assertEquals(1832, service.filtrar("", "", "").size()),
                () -> assertEquals(1832, service.filtrar("  ", " ", "  ").size()));
    }

    @Test
    void pesquisaPeloNomeCivil() {
        List<Candidato> encontrados = service.filtrar("GOVERNADOR", null, "benoni benjamin");

        assertEquals(List.of("BEN MENDES"), nomesUrna(encontrados));
    }

    @Test
    void pesquisaPeloNomeDeUrna() {
        List<Candidato> encontrados = service.filtrar("GOVERNADOR", null, "ben mendes");

        assertEquals(List.of("BEN MENDES"), nomesUrna(encontrados));
    }

    @Test
    void pesquisaPeloNumero() {
        List<Candidato> encontrados = service.filtrar("GOVERNADOR", null, "14");

        assertEquals(List.of("BEN MENDES"), nomesUrna(encontrados));
    }

    @Test
    void combinaCargoPartidoETextoSemDiferenciarMaiusculas() {
        List<Candidato> encontrados = service.filtrar("governador", "missão", "  BeN MeNdEs  ");

        assertEquals(List.of("BEN MENDES"), nomesUrna(encontrados));
    }

    @Test
    void exigeQueTodosOsFiltrosCombinadosSejamAtendidos() {
        assertTrue(service.filtrar("GOVERNADOR", "PT", "BEN MENDES").isEmpty());
    }

    @Test
    void devolveListaVaziaQuandoTextoNaoExiste() {
        assertTrue(service.filtrar(null, null, "candidato inexistente xyz").isEmpty());
    }

    @Test
    void forneceCargosDistintosOrdenados() {
        assertEquals(List.of("1º SUPLENTE", "2º SUPLENTE", "DEPUTADO ESTADUAL",
                "DEPUTADO FEDERAL", "GOVERNADOR", "SENADOR", "VICE-GOVERNADOR"),
                service.listarCargos());
    }

    @Test
    void fornecePartidosDistintosOrdenadosSemValoresVazios() {
        List<String> partidos = service.listarPartidos();

        assertAll(
                () -> assertEquals(29, partidos.size()),
                () -> assertEquals(List.copyOf(new TreeSet<>(partidos)), partidos),
                () -> assertTrue(partidos.containsAll(List.of("MISSÃO", "PT", "UNIÃO"))),
                () -> assertFalse(partidos.stream().anyMatch(String::isBlank)));
    }

    @Test
    void ordenaCandidatosPorNomeDeUrna() {
        List<String> nomes = nomesUrna(service.listarTodos());

        assertEquals(nomes.stream().sorted().toList(), nomes);
        assertEquals("ABNER SOARES", nomes.getFirst());
    }

    @Test
    void emprestaFotoDeOutraCandidaturaDaMesmaPessoa() {
        Candidato candidato = service.filtrar("SENADOR", null, "GUSTAVO GALASSI").getFirst();

        assertAll(
                () -> assertEquals("130002553354", candidato.getSqCandidato()),
                () -> assertEquals("FMG130002554333_div.jpg", candidato.getNomeArquivoFoto()),
                () -> assertTrue(new ClassPathResource(
                        "static/images/candidatos/" + candidato.getNomeArquivoFoto()).exists()));
    }

    private static List<String> nomesUrna(List<Candidato> candidatos) {
        return candidatos.stream().map(Candidato::getNomeUrna).toList();
    }
}
