package com.rodrigo.AppContatos.controller;


import com.rodrigo.AppContatos.models.Contato;
import com.rodrigo.AppContatos.repositories.ContatoRepository;
import com.rodrigo.AppContatos.services.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(("/api/contatos"))
public class ContatoController {

    @Autowired
    ContatoRepository contatoRepository;

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
    public ResponseEntity<?> save(@Valid @RequestBody Contato contato) {
        try {
            Contato novoContato = contatoService.save(contato);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado ao salvar o contato.");
        }
    }

    @Operation(summary = "Atualizar contato por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Contato> update(@PathVariable Long id, @Valid @RequestBody Contato contato) {
        if (!contatoRepository.findById(contato.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado");
        }
        contato.setId(id);
        return ResponseEntity.ok(contatoService.save(contato));
    }

    @Operation(summary = "Deletar contato por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return contatoService.findById(id)
                .map(contato -> {
                    contatoService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado"));
    }

}
