package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.SupervisorRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Supervisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
    @NotBlank
    private String telefone;
    @NotBlank
    private String cargo;
    @Column(name = "status")
    @ColumnDefault("true")
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    @OneToMany(mappedBy = "supervisor")
    private List<Pedido> pedidos;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    @Column(name = "data_hora_ult_atualizacao")
    private LocalDateTime dataHoraUltimaAtualizacao;
    public void criarSupervisor(Supervisor supervisor, Empresa empresa){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        supervisorRepository.criarSupervisor(supervisor, empresa);
    }
    public void vincularEmpresa(Supervisor supervisor, String cnpjEmpresaVinculada){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        supervisorRepository.vincularEmpresa(supervisor, cnpjEmpresaVinculada);
    }
    public void changePasswordSupervisor(String email, String novaSenha){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        supervisorRepository.changePasswordSupervisor(email, novaSenha);
    }
    public void editProfileSupervisor(Supervisor supervisor){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        supervisorRepository.editProfileSupervisor(supervisor);
    }
    public void deleteSupervisor(Supervisor supervisor){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        supervisorRepository.deleteSupervisor(supervisor);
    }
    public Optional<Supervisor> getSupervisorByEmail(String email){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        return supervisorRepository.getSupervisorByEmail(email);
    }
    public Boolean loginSupervisor(String email, String senha){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        return supervisorRepository.loginSupervisor(email,senha);
    }
}
