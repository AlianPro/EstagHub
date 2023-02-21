package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.DiscenteRepositoryImpl;
import br.com.estaghub.repository.impl.EmpresaRepositoryImpl;
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
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String cnpj;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    @OneToMany(mappedBy = "empresa")
    private List<Supervisor> supervisores;
    public List<Empresa> listAllEmpresa(){
        EmpresaRepositoryImpl empresaRepository = new EmpresaRepositoryImpl();
        return empresaRepository.listAllEmpresa();
    }

    public Empresa getEmpresaByCnpj(String cnpj){
        EmpresaRepositoryImpl empresaRepository = new EmpresaRepositoryImpl();
        return empresaRepository.getEmpresaByCnpj(cnpj);
    }

    public void criarEmpresa(Empresa empresa){
        EmpresaRepositoryImpl empresaRepository = new EmpresaRepositoryImpl();
        empresaRepository.criarEmpresa(empresa);
    }

}
