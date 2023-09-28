package br.edu.unifip.ecommerceapi.controllers;

import br.edu.unifip.ecommerceapi.dtos.EnderecoDto;
import br.edu.unifip.ecommerceapi.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/enderco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<EnderecoDto> getEnderecoByUserId(@PathVariable UUID userId) {
        EnderecoDto enderecoDto = enderecoService.getEnderecoByUserId(userId);
        if (enderecoDto != null) {
            return ResponseEntity.ok(enderecoDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EnderecoDto> createEndereco(@PathVariable UUID userId, @RequestBody EnderecoDto addressDTO) {
        EnderecoDto createdEndereco = enderecoService.createEndereco(userId, addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEndereco);
    }

    @PutMapping
    public ResponseEntity<EnderecoDto> updateEndereco(@PathVariable UUID userId, @RequestBody EnderecoDto enderecoDto) {
        EnderecoDto updatedEndereco = enderecoService.updateEndereco(userId, enderecoDto);
        return ResponseEntity.ok(updatedEndereco);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEndereco(@PathVariable UUID userId) {
        enderecoService.deleteEndereco(userId);
        return ResponseEntity.noContent().build();
    }
}