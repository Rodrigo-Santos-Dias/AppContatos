package com.rodrigo.AppContatos.services;

import com.rodrigo.AppContatos.dto.PessoaMalaDiretaDTO;
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
        List<Pessoa> pessoas = pessoaRepository.findAll();
        if (pessoas.isEmpty()) {
            throw new IllegalArgumentException("Não há pessoas cadastradas.");
        }
        return pessoas;
    }

    public Optional<Pessoa> findById(Long id) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);

        if (pessoa.isEmpty()) {
            throw new IllegalArgumentException("Pessoa não encontrada para o id: " + id);
        }

        return pessoa;
    }

    public PessoaMalaDiretaDTO getMalaDireta(Long id) {

        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com o ID: " + id));

        // Formata a mala direta, garantindo que os valores não sejam nulos
        String malaDireta = String.format("%s – CEP: %s – %s/%s",
                pessoa.getEndereco() != null ? pessoa.getEndereco() : "Endereço não informado",
                pessoa.getCep() != null ? pessoa.getCep() : "CEP não informado",
                pessoa.getCidade() != null ? pessoa.getCidade() : "Cidade não informada",
                pessoa.getUf() != null ? pessoa.getUf() : "UF não informada");

        return new PessoaMalaDiretaDTO(pessoa.getId(), pessoa.getNome(), malaDireta);
    }


    public Pessoa update(Pessoa pessoa) {
        if (pessoa == null || pessoa.getId() == null) {
            throw new IllegalArgumentException("O ID da pessoa é obrigatório para atualização.");
        }

        Optional<Pessoa> pessoaExistente = pessoaRepository.findById(pessoa.getId());

        if (pessoaExistente.isEmpty()) {
            return create(pessoa);
        }

        Pessoa updatePessoa = pessoaExistente.get();
        updatePessoa.setNome(pessoa.getNome());
        updatePessoa.setCep(pessoa.getCep());
        updatePessoa.setCidade(pessoa.getCidade());
        updatePessoa.setEndereco(pessoa.getEndereco());
        updatePessoa.setUf(pessoa.getUf());

        return pessoaRepository.save(updatePessoa);
    }



    public Pessoa create(Pessoa pessoa) {
        if (pessoa == null) {
            throw new IllegalArgumentException("Os dados da pessoa não podem ser nulos.");
        }

        if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da pessoa é obrigatório.");
        }



        if (emailExiste(pessoa.getEmail())) {

            throw new IllegalArgumentException("O e-mail já está cadastrado.");
        }

        return pessoaRepository.save(pessoa);
    }




    public void deleteById(Long id) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        if (pessoa.isEmpty()){
            throw new IllegalArgumentException("Pessoa não encontrada");
        }
        pessoaRepository.deleteById(id);
    }

    public boolean emailExiste(String email) {
        Optional<Pessoa> pessoa = pessoaRepository.findByEmail(email);
        return pessoa.isPresent();
    }
}
