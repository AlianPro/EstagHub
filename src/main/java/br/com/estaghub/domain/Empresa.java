package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.EmpresaRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String endereco;
    @NotBlank
    private String telefone;
    @NotBlank
    private String email;
    @NotBlank
    private String cnpj;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    @OneToMany(mappedBy = "empresa")
    private List<Supervisor> supervisores;
    public List<Empresa> listAllEmpresa(){
        EmpresaRepositoryImpl empresaRepository = new EmpresaRepositoryImpl();
        return empresaRepository.listAllEmpresa();
    }

    public Optional<Empresa> getEmpresaByCnpj(String cnpj){
        EmpresaRepositoryImpl empresaRepository = new EmpresaRepositoryImpl();
        return empresaRepository.getEmpresaByCnpj(cnpj);
    }
    public Optional<Empresa> getEmpresaByEmail(String email){
        EmpresaRepositoryImpl empresaRepository = new EmpresaRepositoryImpl();
        return empresaRepository.getEmpresaByEmail(email);
    }
    public Boolean checkIfPossibleToCreateEmpresa(String email, String cnpj){
        EmpresaRepositoryImpl empresaRepository = new EmpresaRepositoryImpl();
        return empresaRepository.checkIfPossibleToCreateEmpresa(email,cnpj);
    }

    public void criarEmpresa(Empresa empresa){
        EmpresaRepositoryImpl empresaRepository = new EmpresaRepositoryImpl();
        empresaRepository.criarEmpresa(empresa);
    }

}
