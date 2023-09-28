package br.edu.unifip.ecommerceapi.repositories;
import br.edu.unifip.ecommerceapi.models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Optional<Endereco> findByUsername_Id(UUID user_id);
}