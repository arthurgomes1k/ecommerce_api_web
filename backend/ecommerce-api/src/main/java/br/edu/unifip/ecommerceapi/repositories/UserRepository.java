package br.edu.unifip.ecommerceapi.repositories;

import br.edu.unifip.ecommerceapi.models.Endereco;
import br.edu.unifip.ecommerceapi.models.Product;
import br.edu.unifip.ecommerceapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    List<User> findByActiveTrue();

    Optional<User> findByEndereco(Endereco endereco);
}
