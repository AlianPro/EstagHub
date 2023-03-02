package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.CursoRepositoryImpl;
import br.com.estaghub.repository.impl.DepartamentoRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String nome;
    private String sigla;
    @OneToMany(mappedBy = "departamento")
    private List<Docente> docentes;
    @OneToMany(mappedBy = "departamento")
    private List<Curso> cursos;
    public void criarDepartamento(Departamento departamento){
        DepartamentoRepositoryImpl departamentoRepository = new DepartamentoRepositoryImpl();
        departamentoRepository.criarDepartamento(departamento);
    }
    public Departamento getDepartamentoById(Long id){
        DepartamentoRepositoryImpl departamentoRepository = new DepartamentoRepositoryImpl();
        return departamentoRepository.getDepartamentoById(id);
    }
    public List<Departamento> getAllDepartamentos(){
        DepartamentoRepositoryImpl departamentoRepository = new DepartamentoRepositoryImpl();
        return departamentoRepository.getAllDepartamentos();
    }
}
