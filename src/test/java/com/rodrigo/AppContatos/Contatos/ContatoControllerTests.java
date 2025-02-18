package com.rodrigo.AppContatos.Contatos;

import com.rodrigo.AppContatos.controller.ContatoController;
import com.rodrigo.AppContatos.models.Contato;
import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.models.TipoContato;
import com.rodrigo.AppContatos.services.ContatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(ContatoController.class)
public class ContatoControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContatoService contatoService;

    private Contato contato;

    private Pessoa pessoa;

    @BeforeEach
    void setup() {
        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Rodrigo Silva");

        contato = new Contato();
        contato.setId(1L);
        contato.setTipoContato(TipoContato.TELEFONE);
        contato.setContato("999999999");
        contato.setPessoa(pessoa);
    }

    @Test
    void deveRetornarListaDeContatos() throws Exception {
        when(contatoService.findAll()).thenReturn(List.of(contato));

        mockMvc.perform(get("/api/contatos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contato").value("999999999"));
    }

    @Test
    void deveRetornarContatoPorId() throws Exception {
        when(contatoService.findById(1L)).thenReturn(contato);

        mockMvc.perform(get("/api/contatos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contato").value("999999999"));
    }

    @Test
    void deveCriarContato() throws Exception {
        when(contatoService.saveOrUpdate(Mockito.any(Contato.class))).thenReturn(contato);

        mockMvc.perform(post("/api/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contato)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contato").value("999999999"));
    }

    @Test
    void deveDeletarContato() throws Exception {
        doNothing().when(contatoService).deleteById(1L);

        mockMvc.perform(delete("/api/contatos/1"))
                .andExpect(status().isNoContent());
    }
}
