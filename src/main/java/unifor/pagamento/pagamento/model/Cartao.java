package unifor.pagamento.pagamento.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "cartoes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cartao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numeroCartao;

    @Column(nullable = false)
    private String nomeTitular;

    @Column(nullable = false, length = 4)
    private String dataValidade; // MMYY

    @Column(nullable = false)
    private String cvv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCartao tipoCartao; // CREDITO ou DEBITO

    @Column(nullable = false)
    private String cpfTitular;

    @Column(name = "id_usuario", nullable = false)
    @JsonProperty("idUsuario")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Long idUsuario;

    @PrePersist
    @PreUpdate
    private void validateFields() {
        if (!numeroCartao.matches("\\d{16}")) {
            throw new IllegalArgumentException("O número do cartão deve conter 16 dígitos numéricos.");
        }
        if (!cpfTitular.matches("\\d{11}")) {
            throw new IllegalArgumentException("O CPF deve conter 11 dígitos numéricos.");
        }
        if (!dataValidade.matches("\\d{4}")) {
            throw new IllegalArgumentException("A data de validade deve estar no formato MMYY (apenas números).");
        }
    }

    @Override
    public String toString() {
        return "Cartao{" +
                "id=" + id +
                ", numeroCartao='" + numeroCartao + '\'' +
                ", nomeTitular='" + nomeTitular + '\'' +
                ", dataValidade='" + dataValidade + '\'' +
                ", cvv='" + cvv + '\'' +
                ", tipoCartao=" + tipoCartao +
                ", cpfTitular='" + cpfTitular + '\'' +
                ", idUsuario=" + idUsuario +
                '}';
    }
} 