package com.example.CandidatosTSE.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

import com.example.CandidatosTSE.model.Candidato;
import com.example.CandidatosTSE.service.CandidatosTseService;

class CandidatosTseControllerTest {

    private CandidatosTseService service;
    private CandidatosTseController controller;

    @BeforeEach
    void prepararController() {
        service = mock(CandidatosTseService.class);
        controller = new CandidatosTseController(service);
        when(service.listarCargos()).thenReturn(List.of("GOVERNADOR", "SENADOR"));
        when(service.listarPartidos()).thenReturn(List.of("MDB", "PT"));
    }

    @Test
    void carregamentoInicialExibeCandidatosEConverteFiltrosAusentesEmTextoVazio() {
        List<Candidato> candidatos = List.of(new Candidato(), new Candidato());
        when(service.filtrar(null, null, null)).thenReturn(candidatos);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.index(null, null, null, model);

        assertAll(
                () -> assertEquals("index", view),
                () -> assertEquals(candidatos, model.get("candidatos")),
                () -> assertEquals(2, model.get("total")),
                () -> assertEquals(List.of("GOVERNADOR", "SENADOR"), model.get("cargos")),
                () -> assertEquals(List.of("MDB", "PT"), model.get("partidos")),
                () -> assertEquals("", model.get("cargoSelecionado")),
                () -> assertEquals("", model.get("partidoSelecionado")),
                () -> assertEquals("", model.get("textoSelecionado")));
    }

    @Test
    void aplicaOsFiltrosNaOrdemDoServiceEPreservaOsValoresSelecionados() {
        List<Candidato> candidatos = List.of(new Candidato());
        when(service.filtrar("GOVERNADOR", "MDB", "Gabriel")).thenReturn(candidatos);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.index("GOVERNADOR", "MDB", "Gabriel", model);

        verify(service).filtrar("GOVERNADOR", "MDB", "Gabriel");
        assertAll(
                () -> assertEquals("index", view),
                () -> assertEquals(candidatos, model.get("candidatos")),
                () -> assertEquals(1, model.get("total")),
                () -> assertEquals("GOVERNADOR", model.get("cargoSelecionado")),
                () -> assertEquals("MDB", model.get("partidoSelecionado")),
                () -> assertEquals("Gabriel", model.get("textoSelecionado")));
    }

    @Test
    void devolveContagemZeroEListaVaziaQuandoNaoHaResultados() {
        when(service.filtrar(null, null, "inexistente")).thenReturn(List.of());
        ExtendedModelMap model = new ExtendedModelMap();

        assertEquals("index", controller.index(null, null, "inexistente", model));
        assertEquals(0, model.get("total"));
        assertEquals(List.of(), model.get("candidatos"));
        assertEquals("inexistente", model.get("textoSelecionado"));
    }
}
