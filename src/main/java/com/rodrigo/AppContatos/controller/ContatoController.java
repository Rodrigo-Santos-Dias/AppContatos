package com.rodrigo.AppContatos.controller;


import com.rodrigo.AppContatos.models.Contato;
import com.rodrigo.AppContatos.services.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @Operation(summary = "Listar todos os contatos")
    @GetMapping
    public ResponseEntity<List<Contato>> findAll() {
        List<Contato> contatos = contatoService.findAll();
        if (contatos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contatos);
    }

    @Operation(summary = "Obter contato por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Contato> findById(@PathVariable Long id) {
        Contato contato = contatoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrada"));
        return ResponseEntity.ok(contato);
    }



    @Operation(summary = "Cadastrar novo contato")
    @PostMapping
    public ResponseEntity<Contato> save(@Valid @RequestBody Contato contato) {
        Contato novoContato = contatoService.save(contato);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
    }

    @Operation(summary = "Atualizar contato por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Contato> update(@PathVariable Long id, @Valid @RequestBody Contato contato) {
        if (!contatoService.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado");
        }
        contato.setId(id);
        return ResponseEntity.ok(contatoService.save(contato));
    }

    @Operation(summary = "Deletar contato por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!contatoService.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado");
        }
        contatoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
