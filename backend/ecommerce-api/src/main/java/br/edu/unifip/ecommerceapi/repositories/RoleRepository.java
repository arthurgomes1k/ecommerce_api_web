package br.edu.unifip.ecommerceapi.repositories;

import br.edu.unifip.ecommerceapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findById(UUID id);

    void delete(Role role);

    List<Role> findByActiveTrue();
}
