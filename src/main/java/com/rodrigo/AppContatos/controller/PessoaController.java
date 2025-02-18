package com.rodrigo.AppContatos.controller;

import com.rodrigo.AppContatos.dto.PessoaMalaDiretaDTO;
import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.services.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;


    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @Operation(summary = "Listar todas as pessoas")
    @GetMapping
    public ResponseEntity<List<Pessoa>> findAll() {
        List<Pessoa> pessoas = pessoaService.findAll();
        return pessoas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pessoas);
    }

    @Operation(summary = "Obter pessoa por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaService.findById(id));
    }

    @Operation(summary = "Obter mala direta por ID")
    @GetMapping("/maladireta/{id}")
    public ResponseEntity<PessoaMalaDiretaDTO> getMalaDireta(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaService.getMalaDireta(id));
    }

    @Operation(summary = "Criar uma nova pessoa")
    @PostMapping
    public ResponseEntity<Pessoa> save(@Valid @RequestBody Pessoa pessoa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.saveOrUpdate(pessoa));
    }

    @Operation(summary = "Atualizar pessoa por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> update(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
        pessoa.setId(id);
        return ResponseEntity.ok(pessoaService.saveOrUpdate(pessoa));
    }

    @Operation(summary = "Deletar pessoa por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        pessoaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
