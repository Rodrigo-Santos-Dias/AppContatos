package com.rodrigo.AppContatos.repositories;

import com.rodrigo.AppContatos.models.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository <Pessoa,Long> {

}
