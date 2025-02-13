package com.rodrigo.AppContatos.services;

import com.rodrigo.AppContatos.models.Contato;
import com.rodrigo.AppContatos.repositories.ContatoRepository;
import com.rodrigo.AppContatos.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ContatoService {
    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Contato> findAll() {
        List<Contato> contatos =   contatoRepository.findAll();
        if (contatos.isEmpty()) {
            throw new IllegalArgumentException("Não há contatos cadastrados.");
        }
        return contatos;
    }


    public Optional<Contato> findById(Long id) {

        Optional<Contato> contato= contatoRepository.findById(id);

        if (contato.isEmpty()) {
            throw new IllegalArgumentException("Contato não encontrado para o id: " + id);
        }

        return contato;
    }

    public Contato update (Contato contato){
        Optional <Contato> findContato = contatoRepository.findById(contato.getId());
        if (findContato.isPresent()){
            Contato updateContato = findContato.get();
            updateContato.setNome(contato.getNome());
            updateContato.setContato(contato.getContato());
            updateContato.setTipoContato(contato.getTipoContato());
            return contatoRepository.save(updateContato);
        }else {
            return contatoRepository.save(contato);
        }
    }

    public Contato save(Contato contato) {
        try {

            if (contato.getPessoa() == null) {
                throw new IllegalArgumentException("O contato deve estar associado a uma pessoa.");
            }

            if (contato.getContato() == null || contato.getTipoContato() == null) {
                throw new IllegalArgumentException("O contato e o tipo de contato não podem ser nulos.");
            }

            boolean contatoDuplicado = contatoRepository.existsByPessoaAndContato(contato.getPessoa(), contato.getContato());
            if (contatoDuplicado) {
                throw new IllegalArgumentException("Esse contato já está cadastrado para essa pessoa.");
            }
            return contatoRepository.save(contato);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro de validação: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Erro inesperado ao salvar o contato.");
            throw new RuntimeException("Erro inesperado ao salvar o contato.");
        }
    }

    public void deleteById(Long id) {
        Optional<Contato> contato = contatoRepository.findById(id);
        if (contato.isPresent()){
            contatoRepository.deleteById(id);
        }else{
            throw new RuntimeException("Contato não encontrado com o id: " + id);
        }
    }


}
