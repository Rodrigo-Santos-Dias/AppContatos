package com.rodrigo.AppContatos.services;

import com.rodrigo.AppContatos.models.Contato;
import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.repositories.ContatoRepository;
import com.rodrigo.AppContatos.repositories.PessoaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final PessoaRepository pessoaRepository;


    public ContatoService(ContatoRepository contatoRepository, PessoaRepository pessoaRepository) {
        this.contatoRepository = contatoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<Contato> findAll() {
        List<Contato> contatos = contatoRepository.findAll();
        if (contatos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não há contatos cadastrados.");
        }
        return contatos;
    }

    public Contato findById(Long id) {
        return contatoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado para o ID: " + id));
    }

    @Transactional
    public Contato saveOrUpdate(Contato contato) {
        if (contato.getPessoa() == null || contato.getContato() == null || contato.getTipoContato() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O contato, o tipo de contato e a pessoa são obrigatórios.");
        }

        Pessoa pessoa = pessoaRepository.findById(contato.getPessoa().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada para o ID: " + contato.getPessoa().getId()));

        boolean contatoDuplicado = contatoRepository.existsByPessoaAndContato(pessoa, contato.getContato());
        if (contatoDuplicado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse contato já está cadastrado para essa pessoa.");
        }

        contato.setPessoa(pessoa);
        return contatoRepository.save(contato);
    }

    @Transactional
    public void deleteById(Long id) {
        Contato contato = findById(id);
        contatoRepository.delete(contato);
    }
}
