package com.rodrigo.AppContatos.controller;

import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.services.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @Operation(summary = "Listar todas as pessoas")
    @GetMapping
    public ResponseEntity<List<Pessoa>> findAll() {
        List<Pessoa> pessoas = pessoaService.findAll();
        if (pessoas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pessoas);
    }

    @Operation(summary = "Obter pessoa por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable Long id) {
        Pessoa pessoa = pessoaService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
        return ResponseEntity.ok(pessoa);
    }



    @Operation(summary = "Criar uma nova pessoa")
    @PostMapping
    public ResponseEntity<Pessoa> save(@Valid @RequestBody Pessoa pessoa) {
        Pessoa novaPessoa = pessoaService.save(pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPessoa);
    }

    @Operation(summary = "Atualizar pessoa por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> update(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
        if (!pessoaService.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada");
        }
        pessoa.setId(id);
        return ResponseEntity.ok(pessoaService.save(pessoa));
    }

    @Operation(summary = "Deletar pessoa por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!pessoaService.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada");
        }
        pessoaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
