package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.DiscenteRepositoryImpl;
import br.com.estaghub.repository.impl.SupervisorRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Supervisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cargo;
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    @OneToMany(mappedBy = "supervisor")
    private List<Pedido> pedidos;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    public void criarSupervisor(Supervisor supervisor, Empresa empresa, String numPedido){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        supervisorRepository.criarSupervisor(supervisor, empresa, numPedido);
    }
    public void vincularEmpresa(Supervisor supervisor, String cnpjEmpresaVinculada, String numPedido){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        supervisorRepository.vincularEmpresa(supervisor, cnpjEmpresaVinculada, numPedido);
    }
    public Long getIdSupervisor(Supervisor supervisor){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        return supervisorRepository.getIdSupervisor(supervisor);
    }
    public Supervisor getSupervisorByEmail(String email){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        return supervisorRepository.getSupervisorByEmail(email);
    }
    public Boolean loginSupervisor(String email, String senha){
        SupervisorRepositoryImpl supervisorRepository = new SupervisorRepositoryImpl();
        return supervisorRepository.loginSupervisor(email,senha);
    }
}
