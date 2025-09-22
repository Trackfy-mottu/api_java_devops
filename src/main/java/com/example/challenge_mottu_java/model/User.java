package com.example.challenge_mottu_java.model;

import com.example.challenge_mottu_java.Enums.RolesUser;
import com.example.challenge_mottu_java.dto.CourtDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O e-mail do usuário não pode ser nulo")
    @Email(message = "O e-mail do usuário deve ser válido")
    private String username;

    @NotBlank(message = "A senha do usuário não pode ser nula")
    @Size(min = 1, max = 100)
    private String password;

    @NotBlank(message = "O nome do usuário não pode ser nulo")
    @Size(min = 1, max = 255)
    private String name;

    @NotNull(message = "O pátio do usuário é obrigatório")
    @ManyToOne(optional = false)
    @JoinColumn(name = "court_acess_code", referencedColumnName = "acess_code")
    private Court court;

    @NotNull(message = "O papel do usuário é obrigatório")
    @Enumerated(EnumType.STRING)
    private RolesUser role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

}
