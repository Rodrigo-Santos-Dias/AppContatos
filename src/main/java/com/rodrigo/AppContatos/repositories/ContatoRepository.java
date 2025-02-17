package com.rodrigo.AppContatos.repositories;

import com.rodrigo.AppContatos.models.Contato;
import com.rodrigo.AppContatos.models.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContatoRepository extends JpaRepository<Contato,Long> {

    boolean existsByPessoaAndContato(Pessoa pessoa, String contato);

    List<Contato>findAllByPessoa(Pessoa pessoa);
}
