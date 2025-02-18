package com.rodrigo.AppContatos.Pessoa;

import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.repositories.PessoaRepository;
import com.rodrigo.AppContatos.services.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoa;

    @BeforeEach
    void setup() {
        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Rodrigo Dias");
        pessoa.setEndereco("Rua A, 123");
        pessoa.setCep("59910981");
        pessoa.setCidade("São Paulo");
        pessoa.setUf("SP");
    }


    @Test
    void deveRetornarTodasAsPessoas() {
        when(pessoaRepository.findAll()).thenReturn(List.of(pessoa));
        List<Pessoa> resultado = pessoaService.findAll();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Rodrigo Dias", resultado.get(0).getNome());
    }

    @Test
    void deveLancarErroAoBuscarPessoaInexistente() {
        when(pessoaRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> pessoaService.findById(2L));
    }

    @Test
    void deveSalvarPessoa() {
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        Pessoa pessoaSalva = pessoaService.saveOrUpdate(pessoa);
        assertNotNull(pessoaSalva);
        assertEquals("Rodrigo Dias", pessoaSalva.getNome());
    }

    @Test
    void deveDeletarPessoaExistente() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        assertDoesNotThrow(() -> pessoaService.deleteById(1L));
        verify(pessoaRepository, times(1)).delete(pessoa);
    }
}