package br.com.estaghub.domain;

import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.repository.impl.DiscenteRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String nome;
    private String matricula;
    private String email;
    private String senha;
    private String telefone;
    @ManyToOne
    @JoinColumn(name="curso_id")
    private Curso curso;
    private String periodo;
    private String rg;
    @Column(name = "orgao_expedidor_rg")
    private String orgaoExpedidorRg;
    private String cpf;
    private String endereco;
    private String ira;
    @Column(name = "carga_horaria_cumprida")
    private String cargaHorariaCumprida;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    @OneToMany(mappedBy = "discente")
    private List<Pedido> pedidos;
    public void criarDiscente(Discente discente){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        discenteRepository.criarDiscente(discente);
    }
    public Boolean loginDiscente(String email, String senha){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        return discenteRepository.loginDiscente(email,senha);
    }
    public Optional<Discente> getDiscenteByEmail(String email){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        return discenteRepository.getDiscenteByEmail(email);
    }
    public Boolean checkIfDiscenteAlreadyHavePedido(Discente discente, TipoPedido tipoPedido){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        return discenteRepository.checkIfDiscenteAlreadyHavePedido(discente, tipoPedido);
    }
    public void addInfoNovoPedidoInDiscente(Discente discente){
        DiscenteRepositoryImpl discenteRepository = new DiscenteRepositoryImpl();
        discenteRepository.addInfoNovoPedidoInDiscente(discente);
    }
}
