package br.edu.unifip.ecommerceapi.services;

import br.edu.unifip.ecommerceapi.models.Role;
import br.edu.unifip.ecommerceapi.models.User;
import br.edu.unifip.ecommerceapi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    final UserRepository userRepository;
    final BCryptPasswordEncoder bCryptPasswordEncoder;
    final RoleService roleService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }

    public List<User> findAll() {
        return userRepository.findByActiveTrue();
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User save(User user, List<UUID> roleIds) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // Verificar se as roles existem e adicioná-las ao usuário
        if (roleIds != null) {
            for (UUID roleId : roleIds) {
                Optional<Role> roleOptional = roleService.findById(roleId);
                if (roleOptional.isPresent()) {
                    Role role = roleOptional.get();
                    user.getRoles().add(role);
                }
            }
        }
        return userRepository.save(user);
    }

    @Transactional
    public void hardDelete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void softDelete(User user) {
        Optional<User> userInstance = userRepository.findById(user.getId());
        userInstance.ifPresent(value -> value.setActive(false));
    }

    public User partialUpdate(User user, Map<Object, Object> objectMap) {
        objectMap.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, (String) key);
            field.setAccessible(true);

            if ("password".equals(key) && value != null) {
                // Criptografar a senha com BCrypt
                String encryptedPassword = bCryptPasswordEncoder.encode((String) value);
                ReflectionUtils.setField(field, user, encryptedPassword);
            } else {
                try {
                    value = BigDecimal.valueOf((double) value);
                } catch (ClassCastException ignored) {
                }
                ReflectionUtils.setField(field, user, value);
            }
        });
        return userRepository.save(user);
    }


    public List<User> findByActiveTrue() {
        return userRepository.findByActiveTrue();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
