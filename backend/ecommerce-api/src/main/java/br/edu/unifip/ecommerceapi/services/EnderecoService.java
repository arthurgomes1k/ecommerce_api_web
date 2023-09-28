package br.edu.unifip.ecommerceapi.services;

import br.edu.unifip.ecommerceapi.dtos.EnderecoDto;
import br.edu.unifip.ecommerceapi.models.Endereco;
import br.edu.unifip.ecommerceapi.models.User;
import br.edu.unifip.ecommerceapi.repositories.EnderecoRepository;
import br.edu.unifip.ecommerceapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    private final UserRepository userRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, UserRepository userRepository) {
        this.enderecoRepository = enderecoRepository;
        this.userRepository = userRepository;
    }

    public EnderecoDto getEnderecoByUserId(UUID userId) {
        Optional<Endereco> endereco = enderecoRepository.findByUsername_Id(userId);
        return endereco.map(this::convertToDto).orElse(null);
    }

    public EnderecoDto createEndereco(UUID userId, EnderecoDto enderecoDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Endereco endereco = convertToEntity(enderecoDto);
        endereco.setUsername(user);

        return convertToDto(enderecoRepository.save(endereco));
    }

    public EnderecoDto updateEndereco(UUID userId, EnderecoDto enderecoDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Endereco existingEndereco = enderecoRepository.findByUsername_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        existingEndereco.setRua(enderecoDto.rua());
        existingEndereco.setCidade(enderecoDto.cidade());

        return convertToDto(enderecoRepository.save(existingEndereco));
    }

    public void deleteEndereco(UUID userId) {
        Endereco endereco = enderecoRepository.findByUsername_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        enderecoRepository.delete(endereco);
    }

    private EnderecoDto convertToDto(Endereco endereco) {
        return new EnderecoDto(endereco.getUsername().getId(), endereco.getRua(), endereco.getCidade());
    }

    private Endereco convertToEntity(EnderecoDto enderecoDto) {
        Endereco endereco = new Endereco();
        endereco.setRua(enderecoDto.rua());
        endereco.setCidade(enderecoDto.cidade());
        return endereco;
    }
}