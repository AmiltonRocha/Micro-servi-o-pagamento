package unifor.pagamento.pagamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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

    @Column(nullable = false, length = 5)
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Data de validade deve estar no formato MM/YY")
    private String dataValidade;

    @Column(nullable = false)
    private String cvv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCartao tipoCartao; // CREDITO ou DEBITO

    @Column(nullable = false)
    private String cpfTitular;

    @Column(name = "id_usuario", nullable = false)
    @JsonProperty(value = "idUsuario", required = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Long idUsuario;

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