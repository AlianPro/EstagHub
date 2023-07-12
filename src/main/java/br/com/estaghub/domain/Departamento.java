package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.DepartamentoRepositoryImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String sigla;
    @Column(name = "status")
    @ColumnDefault("true")
    private Boolean isActive;
    @Column(name = "data_hora_ult_atualizacao")
    private LocalDateTime dataHoraUltimaAtualizacao;
    @OneToMany(mappedBy = "departamento")
    private List<Docente> docentes;
    @OneToMany(mappedBy = "departamento")
    private List<Curso> cursos;
    public void criarDepartamento(Departamento departamento){
        DepartamentoRepositoryImpl departamentoRepository = new DepartamentoRepositoryImpl();
        departamentoRepository.criarDepartamento(departamento);
    }
    public void updateDepartamento(Departamento departamento){
        DepartamentoRepositoryImpl departamentoRepository = new DepartamentoRepositoryImpl();
        departamentoRepository.updateDepartamento(departamento);
    }
    public Boolean checkIfListOfDepartamentoAlreadyHaveThatDepartamento(String nameDepartamento, String siglaDepartamento){
        DepartamentoRepositoryImpl departamentoRepository = new DepartamentoRepositoryImpl();
        return departamentoRepository.checkIfListOfDepartamentoAlreadyHaveThatDepartamento(nameDepartamento, siglaDepartamento);
    }
    public Departamento getDepartamentoById(Long id){
        DepartamentoRepositoryImpl departamentoRepository = new DepartamentoRepositoryImpl();
        return departamentoRepository.getDepartamentoById(id);
    }
    public List<Departamento> getAllDepartamentos(){
        DepartamentoRepositoryImpl departamentoRepository = new DepartamentoRepositoryImpl();
        return departamentoRepository.getAllDepartamentos();
    }
    public List<Departamento> getAllDepartamentosWithStatusActive(){
        DepartamentoRepositoryImpl departamentoRepository = new DepartamentoRepositoryImpl();
        return departamentoRepository.getAllDepartamentosWithStatusActive();
    }
}
