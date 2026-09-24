package com.example.CandidatosTSE.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CandidatoTest {

    @Test
    void calculaIdadeNoDiaDoAniversario() {
        Candidato candidato = new Candidato();
        candidato.setDtNascimento(LocalDate.now().minusYears(30)
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        assertEquals(30, candidato.getIdade());
    }

    @Test
    void consideraSeOAniversarioDoAnoJaOcorreu() {
        Candidato candidato = new Candidato();
        candidato.setDtNascimento("15/06/2000");
        LocalDate hoje = LocalDate.now();
        int idadeEsperada = hoje.getYear() - 2000;
        if (hoje.isBefore(LocalDate.of(hoje.getYear(), 6, 15))) {
            idadeEsperada--;
        }

        assertEquals(idadeEsperada, candidato.getIdade());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "#NULO", "data inválida", "99/99/2000" })
    void omiteIdadeQuandoDataEstaAusenteOuInvalida(String data) {
        Candidato candidato = new Candidato();
        candidato.setDtNascimento(data);

        assertEquals(-1, candidato.getIdade());
    }

    @Test
    void usaSequencialDaCandidaturaParaNomeDaFoto() {
        Candidato candidato = new Candidato();
        candidato.setSqCandidato("130002539775");

        assertEquals("FMG130002539775_div.jpg", candidato.getNomeArquivoFoto());
    }

    @Test
    void priorizaFotoEmprestadaQuandoDefinida() {
        Candidato candidato = new Candidato();
        candidato.setSqCandidato("130002553354");
        candidato.setSqCandidatoParaFoto("130002554333");

        assertEquals("FMG130002554333_div.jpg", candidato.getNomeArquivoFoto());
    }
}
