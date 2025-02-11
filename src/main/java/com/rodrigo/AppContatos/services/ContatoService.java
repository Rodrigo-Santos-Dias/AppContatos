package com.rodrigo.AppContatos.services;

import com.rodrigo.AppContatos.models.Contato;
import com.rodrigo.AppContatos.repositories.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContatoService {
    @Autowired
    private ContatoRepository contatoRepository;

    public List<Contato> findAll() {
        return contatoRepository.findAll();
    }

    public Optional<Contato> findById(Long id) {
        return contatoRepository.findById(id);
    }

    public Contato save(Contato contato) {
        return contatoRepository.save(contato);
    }

    public void deleteById(Long id) {
        contatoRepository.deleteById(id);
    }


}
