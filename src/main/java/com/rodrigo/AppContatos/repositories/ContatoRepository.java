package com.rodrigo.AppContatos.repositories;

import com.rodrigo.AppContatos.models.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato,Long> {
}
