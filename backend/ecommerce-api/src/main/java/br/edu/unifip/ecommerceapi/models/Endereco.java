package br.edu.unifip.ecommerceapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "TB_ENDERECO")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rua;
    private String cidade;
    private String estado;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User username;
}