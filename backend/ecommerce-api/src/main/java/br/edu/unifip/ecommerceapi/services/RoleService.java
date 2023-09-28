package br.edu.unifip.ecommerceapi.services;

import br.edu.unifip.ecommerceapi.models.Role;
import br.edu.unifip.ecommerceapi.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Service
public class RoleService {

    final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findById(UUID id) {
        return roleRepository.findById(id);
    }

    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public void hardDelete(Role role) {
        roleRepository.delete(role);
    }

    @Transactional
    public void softDelete(Role role) {
        Optional<Role> roleInstance = roleRepository.findById(role.getId());
        roleInstance.ifPresent(value -> value.setActive(false));
    }

    public Role partialUpdate(Role role, Map<Object, Object> objectMap) {
        objectMap.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Role.class, (String) key);
            field.setAccessible(true);

            try {
                value = BigDecimal.valueOf((double) value);
            } catch (ClassCastException ignored) {
            }
            ReflectionUtils.setField(field, role, value);
        });
        return roleRepository.save(role);
    }

    public List<Role> findByActiveTrue() {
        return roleRepository.findByActiveTrue();
    }
}
