package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.DiscenteRepositoryImpl;
import br.com.estaghub.repository.impl.DocenteRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String nome;
    private String siape;
    private String email;
    private String senha;
    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    @Column(name = "docente_comissao")
    private Boolean isDocenteComissao;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
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
}
