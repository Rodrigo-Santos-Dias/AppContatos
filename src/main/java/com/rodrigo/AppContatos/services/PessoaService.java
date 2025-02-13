package com.rodrigo.AppContatos.services;

import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public Optional<Pessoa> findById(Long id) {
        return pessoaRepository.findById(id);
    }

    public Pessoa update (Pessoa pessoa){
        Optional<Pessoa> pessoaJaExistente = pessoaRepository.findById(pessoa.getId());

        if (pessoaJaExistente.isPresent()){
            Pessoa updatePessoa = pessoaJaExistente.get();
            updatePessoa.setNome(pessoa.getNome());
            updatePessoa.setCep(pessoa.getCep());
            updatePessoa.setCidade(pessoa.getCidade());
            updatePessoa.setEndereco(pessoa.getEndereco());
            updatePessoa.setUf(pessoa.getUf());
            return pessoaRepository.save(updatePessoa);
        }else {
            return pessoaRepository.save(pessoa);
        }
    }

    public Pessoa save(Pessoa pessoa) {
        Optional<Pessoa> pessoaJaExistente  = pessoaRepository.findById(pessoa.getId());
        if (pessoaJaExistente.isPresent()){
            System.out.println("Pessoa ja Cadastrada no banco de dados");
            return pessoaJaExistente.get();
        }else{
            return pessoaRepository.save(pessoa);
        }
    }

    public void deleteById(Long id) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        if (pessoa.isEmpty()){
            throw new RuntimeException("Pessoa não encontrada");
        }
        pessoaRepository.deleteById(id);
    }
}
