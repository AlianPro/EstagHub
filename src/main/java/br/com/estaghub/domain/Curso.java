package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.CursoRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @OneToMany(mappedBy = "curso")
    private List<Discente> discentes;
    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    public Optional<Curso> getCursoById(Long id){
        CursoRepositoryImpl cursoRepository = new CursoRepositoryImpl();
        return cursoRepository.getCursoById(id);
    }
    public List<Curso> getAllCursos(){
        CursoRepositoryImpl cursoRepository = new CursoRepositoryImpl();
        return cursoRepository.getAllCursos();
    }
    public void criarCurso(Curso curso){
        CursoRepositoryImpl cursoRepository = new CursoRepositoryImpl();
        cursoRepository.criarCurso(curso);
    }
}
