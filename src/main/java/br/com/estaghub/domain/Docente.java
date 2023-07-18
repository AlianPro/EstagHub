package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.DocenteRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String email;
    @NotBlank
    private String siape;
    @NotBlank
    private String senha;
    @ManyToOne
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;
    @Column(name = "docente_comissao")
    private Boolean isDocenteComissao;
    @Column(name = "status")
    @ColumnDefault("true")
    private Boolean isActive;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    @Column(name = "data_hora_ult_atualizacao")
    private LocalDateTime dataHoraUltimaAtualizacao;
    @ManyToOne
    @JoinColumn(name = "id_docente_responsavel_criacao")
    private Docente docenteResponsavelCriacao;
    @OneToMany(mappedBy = "docenteComissaoResponsavel")
    @Column(name = "pedidos_docente_comissao_responsavel")
    private List<Pedido> pedidosDocenteComissaoResponsavel;
    @OneToMany(mappedBy = "docenteOrientador")
    @Column(name = "pedidos_docente_orientador")
    private List<Pedido> pedidosDocenteOrientador;
    public void criarDocente(Docente docente){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        docenteRepository.criarDocente(docente);
    }
    public void updateDocente(Docente docente){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        docenteRepository.updateDocente(docente);
    }
    public void changePasswordDocente(String email, String novaSenha){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        docenteRepository.changePasswordDocente(email, novaSenha);
    }
    public void editProfileDocente(Docente docente){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        docenteRepository.editProfileDocente(docente);
    }
    public void deleteDocente(Docente docente){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        docenteRepository.deleteDocente(docente);
    }
    public Boolean loginDocente(String email, String senha){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        return docenteRepository.loginDocente(email,senha);
    }
    public Boolean checkIfDocenteIsComissao(String email){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        return docenteRepository.checkIfDocenteIsComissao(email);
    }
    public Optional<Docente> getDocenteByEmail(String email){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        return docenteRepository.getDocenteByEmail(email);
    }
    public Boolean checkSiapeOfDocente(Docente docente){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        return docenteRepository.checkSiapeOfDocente(docente);
    }
    public List<Docente> getAllDocentes(){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        return docenteRepository.getAllDocentes();
    }
    public List<Docente> getAllDocentesOutComissao(){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        return docenteRepository.getAllDocentesOutComissao();
    }
    public List<Docente> getAllDocentesOfComissaoFromThisDepartamento(Departamento departamento){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        return docenteRepository.getAllDocentesOfComissaoFromThisDepartamento(departamento);
    }
    public Optional<Docente> getDocenteById(Long id){
        DocenteRepositoryImpl docenteRepository = new DocenteRepositoryImpl();
        return docenteRepository.getDocenteById(id);
    }
}
