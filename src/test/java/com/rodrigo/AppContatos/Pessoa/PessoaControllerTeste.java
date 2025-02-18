package com.rodrigo.AppContatos.Pessoa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.AppContatos.controller.PessoaController;
import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.services.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PessoaController.class)
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;


    @MockitoBean
    private PessoaService pessoaService;

    private Pessoa pessoa;

    @BeforeEach
    void setup() {
        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Rodrigo Silva");
        pessoa.setEndereco("Rua A, 123");
        pessoa.setCep("59910981");
        pessoa.setCidade("São Paulo");
        pessoa.setUf("SP");
    }

    @Test
    void deveRetornarListaDePessoas() throws Exception {
        when(pessoaService.findAll()).thenReturn(List.of(pessoa));

        mockMvc.perform(get("/api/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Rodrigo Silva"));
    }

    @Test
    void deveRetornarPessoaPorId() throws Exception {
        when(pessoaService.findById(1L)).thenReturn(pessoa);

        mockMvc.perform(get("/api/pessoas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Rodrigo Silva"));
    }

    @Test
    void deveCriarPessoa() throws Exception {
        when(pessoaService.saveOrUpdate(Mockito.any(Pessoa.class))).thenReturn(pessoa);

        mockMvc.perform(post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pessoa)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Rodrigo Silva"));
    }
}
