package com.example.CandidatosTSE.controller;

import com.example.CandidatosTSE.model.Candidato;
import com.example.CandidatosTSE.service.CandidatosTseService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CandidatosTseController {

    private final CandidatosTseService candidatosTseService;

    public CandidatosTseController(CandidatosTseService candidatosTseService) {
        this.candidatosTseService = candidatosTseService;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(name = "cargo", required = false) String cargo,
            @RequestParam(name = "partido", required = false) String partido,
            @RequestParam(name = "texto", required = false) String texto,
            Model model) {
        List<Candidato> candidatos = candidatosTseService.filtrar(cargo, partido, texto);

        model.addAttribute("candidatos", candidatos);
        model.addAttribute("total", candidatos.size());
        model.addAttribute("cargos", candidatosTseService.listarCargos());
        model.addAttribute("partidos", candidatosTseService.listarPartidos());
        model.addAttribute("cargoSelecionado", cargo == null ? "" : cargo);
        model.addAttribute("partidoSelecionado", partido == null ? "" : partido);
        model.addAttribute("textoSelecionado", texto == null ? "" : texto);

        return "index";
    }
}
