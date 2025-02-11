package com.rodrigo.AppContatos.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_contato")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private TipoContato tipoContato;

    @Column(nullable = false)
    private String contato;
}
