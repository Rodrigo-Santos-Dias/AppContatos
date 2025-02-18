package com.rodrigo.AppContatos.services;

import com.rodrigo.AppContatos.dto.PessoaMalaDiretaDTO;
import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.repositories.PessoaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public List<Pessoa> findAll() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        if (pessoas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não há pessoas cadastradas.");
        }
        return pessoas;
    }

    public Pessoa findById(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada para o ID: " + id));
    }

    public PessoaMalaDiretaDTO getMalaDireta(Long id) {
        Pessoa pessoa = findById(id);

        String malaDireta = String.format("%s – CEP: %s – %s/%s",
                pessoa.getEndereco() != null ? pessoa.getEndereco() : "Endereço não informado",
                pessoa.getCep() != null ? pessoa.getCep() : "CEP não informado",
                pessoa.getCidade() != null ? pessoa.getCidade() : "Cidade não informada",
                pessoa.getUf() != null ? pessoa.getUf() : "UF não informada");

        return new PessoaMalaDiretaDTO(pessoa.getId(), pessoa.getNome(), malaDireta);
    }

    @Transactional
    public Pessoa saveOrUpdate(Pessoa pessoa) {
        if (pessoa == null || pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome da pessoa é obrigatório.");
        }
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public void deleteById(Long id) {
        Pessoa pessoa = findById(id);
        pessoaRepository.delete(pessoa);
    }
}
