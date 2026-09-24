package com.example.CandidatosTSE;

import com.example.CandidatosTSE.application.CandidatosTseApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CandidatosTseApplication.class)
@AutoConfigureMockMvc
class PaginaCandidatosTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void renderizaPaginaInicialComTodosOsCandidatos() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("total", 1832))
                .andExpect(content().string(containsString("1832 candidato(s) encontrado(s).")))
                .andExpect(content().string(containsString("ABNER SOARES")))
                .andExpect(content().string(not(containsString("#NE"))))
                .andExpect(content().string(not(containsString("#NULO"))))
                .andExpect(content().string(not(containsString("NÃO DIVULGÁVEL"))))
                .andExpect(content().string(not(containsString("th:each"))));
    }

    @Test
    void filtraGovernadoresESenadores() throws Exception {
        mvc.perform(get("/").param("cargo", "GOVERNADOR"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("total", 11))
                .andExpect(content().string(containsString("ALEXANDRE KALIL")));
        mvc.perform(get("/").param("cargo", "SENADOR"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("total", 18))
                .andExpect(content().string(containsString("FMG130002554333_div.jpg")));
    }

    @Test
    void combinaFiltrosEPreservaValoresNoHtml() throws Exception {
        mvc.perform(get("/").param("cargo", "GOVERNADOR")
                        .param("partido", "MDB").param("texto", "Gabriel"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("total", 1))
                .andExpect(content().string(containsString("value=\"Gabriel\"")))
                .andExpect(content().string(containsString("value=\"GOVERNADOR\" selected=\"selected\"")))
                .andExpect(content().string(containsString("value=\"MDB\" selected=\"selected\"")))
                .andExpect(content().string(containsString("GABRIEL SOUSA MARQUES DE AZEVEDO")));
    }

    @Test
    void informaBuscaSemResultadoEEscapaTextoDigitado() throws Exception {
        mvc.perform(get("/").param("texto", "<script>alert(1)</script>"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("total", 0))
                .andExpect(content().string(containsString("Nenhum candidato encontrado.")))
                .andExpect(content().string(containsString("&lt;script&gt;")))
                .andExpect(content().string(not(containsString("<script>"))));
    }

    @Test
    void serveEstiloFotosEImagemSubstituta() throws Exception {
        mvc.perform(get("/css/style.css"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("aspect-ratio: 161 / 225")));
        mvc.perform(get("/images/candidatos/FMG130002554333_div.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("image/jpeg"));
        mvc.perform(get("/images/sem-foto.svg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sem foto")));
    }
}
