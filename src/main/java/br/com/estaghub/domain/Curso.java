package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.CursoRepositoryImpl;
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
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @OneToMany(mappedBy = "curso")
    private List<Discente> discentes;
    @Column(name = "status")
    @ColumnDefault("true")
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;
    @Column(name = "data_hora_ult_atualizacao")
    private LocalDateTime dataHoraUltimaAtualizacao;
    public Optional<Curso> getCursoById(Long id){
        CursoRepositoryImpl cursoRepository = new CursoRepositoryImpl();
        return cursoRepository.getCursoById(id);
    }
    public Boolean checkIfDepartamentoAlreadyHaveThatCourse(String nameCourse, Departamento departamento){
        CursoRepositoryImpl cursoRepository = new CursoRepositoryImpl();
        return cursoRepository.checkIfDepartamentoAlreadyHaveThatCourse(nameCourse,departamento);
    }
    public void updateCurso(Curso curso){
        CursoRepositoryImpl cursoRepository = new CursoRepositoryImpl();
        cursoRepository.updateCurso(curso);
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
