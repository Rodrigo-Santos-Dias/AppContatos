package com.rodrigo.AppContatos.controller;

import com.rodrigo.AppContatos.models.Contato;
import com.rodrigo.AppContatos.services.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contatos")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @Operation(summary = "Listar todos os contatos")
    @GetMapping
    public ResponseEntity<List<Contato>> findAll() {
        List<Contato> contatos = contatoService.findAll();
        return contatos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(contatos);
    }

    @Operation(summary = "Obter contato por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Contato> findById(@PathVariable Long id) {
        return ResponseEntity.ok(contatoService.findById(id));
    }

    @Operation(summary = "Cadastrar novo contato")
    @PostMapping
    public ResponseEntity<Contato> save(@Valid @RequestBody Contato contato) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoService.saveOrUpdate(contato));
    }

    @Operation(summary = "Atualizar contato por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Contato> update(@PathVariable Long id, @Valid @RequestBody Contato contato) {
        contato.setId(id);
        return ResponseEntity.ok(contatoService.saveOrUpdate(contato));
    }

    @Operation(summary = "Deletar contato por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        contatoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
