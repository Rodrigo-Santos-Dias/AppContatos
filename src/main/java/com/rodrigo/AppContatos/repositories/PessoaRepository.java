package com.rodrigo.AppContatos.repositories;

import com.rodrigo.AppContatos.models.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository <Pessoa,Long> {
    public Optional<Pessoa>findByEmail(String nome);
}
