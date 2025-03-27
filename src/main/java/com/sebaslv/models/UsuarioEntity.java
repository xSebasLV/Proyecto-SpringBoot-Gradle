package com.sebaslv.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Getter @Setter @Column(name = "nombre")
    private String nombre;

    @NotBlank
    @Size(max = 30)
    @Getter @Setter @Column(name = "apellido")
    private String apellido;

    @Email
    @NotBlank
    @Size(max = 80)
    @Getter @Setter @Column(name = "email")
    private String email;

    @NotBlank
    @Size(max = 13)
    @Getter @Setter @Column(name = "telefono")
    private String telefono;

    @NotBlank
    @Getter @Setter @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;
}
