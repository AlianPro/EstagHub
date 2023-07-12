package br.com.estaghub.domain;

import br.com.estaghub.repository.impl.DiscenteRepositoryImpl;
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
public class Discente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String matricula;
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
    @NotBlank
    private String telefone;
    @ManyToOne
    @JoinColumn(name="curso_id")
    private Curso curso;
    @NotBlank
    private String periodo;
    @NotBlank
    private String rg;
    @Column(name = "orgao_expedidor_rg")
    @NotBlank
    private String orgaoExpedidorRg;
    @NotBlank
    private String cpf;
    @NotBlank
    private String endereco;
    @NotBlank
    private String ira;
    @Column(name = "carga_horaria_cumprida")
    @NotBlank
    private String cargaHorariaCumprida;
    @Column(name = "status")
    @ColumnDefault("true")
    private Boolean isActive;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    @Column(name = "data_hora_ult_atualizacao")
    private LocalDateTime dataHoraUltimaAtualizacao;
    @OneToMany(mappedBy = "discente")
    private List<Pedido> pedidos;
    public void criarDiscente(Discente discente){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        discenteRepository.criarDiscente(discente);
    }
    public Boolean checkIfPossibleToCreateDiscente(Discente discente){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        return discenteRepository.checkIfPossibleToCreateDiscente(discente);
    }
    public Boolean loginDiscente(String email, String senha){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        return discenteRepository.loginDiscente(email,senha);
    }
    public Optional<Discente> getDiscenteByEmail(String email){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        return discenteRepository.getDiscenteByEmail(email);
    }
    public Optional<Discente> getDiscenteByMatricula(String matricula){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        return discenteRepository.getDiscenteByMatricula(matricula);
    }
    public void editProfileDiscente(Discente discente){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        discenteRepository.editProfileDiscente(discente);
    }
    public void deleteDiscente(Discente discente){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        discenteRepository.deleteDiscente(discente);
    }
    public void addInfoNovoPedidoInDiscente(Discente discente){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        discenteRepository.addInfoNovoPedidoInDiscente(discente);
    }
    public void changePasswordDiscente(String email, String novaSenha){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        discenteRepository.changePasswordDiscente(email, novaSenha);
    }
    public Optional<Discente> getDiscenteById(Long id){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        return discenteRepository.getDiscenteById(id);
    }
}
