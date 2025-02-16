package com.rodrigo.AppContatos.controller;

import com.rodrigo.AppContatos.dto.PessoaMalaDiretaDTO;
import com.rodrigo.AppContatos.models.Pessoa;
import com.rodrigo.AppContatos.repositories.PessoaRepository;
import com.rodrigo.AppContatos.services.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping("/maladireta/{id}")
    public ResponseEntity<?> getMalaDireta(@Valid @PathVariable Long id) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);

        if (pessoa.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pessoa não encontrada com o ID: " + id);
        }

        PessoaMalaDiretaDTO malaDiretaDTO = pessoaService.getMalaDireta(id);
        return ResponseEntity.ok(malaDiretaDTO);
    }

    @Operation(summary = "Listar todas as pessoas")
    @GetMapping
    public ResponseEntity<List<Pessoa>> findAll() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        if (pessoas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pessoas);
    }

    @Operation(summary = "Obter pessoa por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
        return ResponseEntity.ok(pessoa);
    }

    @Operation(summary = "Criar uma nova pessoa")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Pessoa pessoa) {
        try {
            Pessoa novaPessoa = pessoaService.create(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaPessoa);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar a requisição.");
        }
    }



    @Operation(summary = "Atualizar pessoa por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> update(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
        if (!pessoaRepository.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada");
        }
        pessoa.setId(id);
        return ResponseEntity.ok(pessoaRepository.save(pessoa));
    }

    @Operation(summary = "Deletar pessoa por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!pessoaRepository.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada");
        }
        pessoaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
