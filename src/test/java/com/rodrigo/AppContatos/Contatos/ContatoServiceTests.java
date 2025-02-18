package com.rodrigo.AppContatos.Contatos;

import com.rodrigo.AppContatos.models.Contato;
import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.models.TipoContato;
import com.rodrigo.AppContatos.repositories.ContatoRepository;
import com.rodrigo.AppContatos.repositories.PessoaRepository;
import com.rodrigo.AppContatos.services.ContatoService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContatoServiceTests {

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private PessoaRepository pessoaRepository;


    @InjectMocks
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
        contato.setTipoContato(TipoContato.CELULAR);
        contato.setContato("999999999");
        contato.setPessoa(pessoa);
    }

    @Test
    void deveRetornarTodosOsContatos() {
        when(contatoRepository.findAll()).thenReturn(List.of(contato));

        List<Contato> contatos = contatoService.findAll();

        assertFalse(contatos.isEmpty());
        assertEquals(1, contatos.size());
        assertEquals("999999999", contatos.get(0).getContato());
    }

    @Test
    void deveLancarErroSeNaoExistiremContatos() {
        when(contatoRepository.findAll()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> contatoService.findAll());
    }

    @Test
    void deveRetornarContatoPorId() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));

        Contato resultado = contatoService.findById(1L);

        assertNotNull(resultado);
        assertEquals("999999999", resultado.getContato());
    }

    @Test
    void deveLancarErroSeContatoNaoExistir() {
        when(contatoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> contatoService.findById(2L));
    }

    @Test
    void deveCriarNovoContato() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(contatoRepository.existsByPessoaAndContato(any(), any())).thenReturn(false);
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato novoContato = contatoService.saveOrUpdate(contato);

        assertNotNull(novoContato);
        assertEquals("999999999", novoContato.getContato());
    }

    @Test
    void deveLancarErroAoCriarContatoDuplicado() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(contatoRepository.existsByPessoaAndContato(any(), any())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> contatoService.saveOrUpdate(contato));
    }

    @Test
    void deveDeletarContato() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));
        doNothing().when(contatoRepository).delete(contato);

        assertDoesNotThrow(() -> contatoService.deleteById(1L));
        verify(contatoRepository, times(1)).delete(contato);
    }

    @Test
    void deveLancarErroAoTentarDeletarContatoInexistente() {
        when(contatoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> contatoService.deleteById(2L));
        verify(contatoRepository, never()).delete(any());
    }
}


