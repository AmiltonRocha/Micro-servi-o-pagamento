package unifor.pagamento.pagamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import unifor.pagamento.pagamento.model.TipoCartao;

@Data
public class CartaoDTO {

    private Long id;

    @NotBlank(message = "O número do cartão é obrigatório")
    @Pattern(regexp = "\\d{16}", message = "O número do cartão deve conter 16 dígitos")
    private String numeroCartao;

    @NotBlank(message = "O nome do titular é obrigatório")
    private String nomeTitular;

    @NotBlank(message = "A data de validade é obrigatória")
    @Pattern(regexp = "\\d{4}", message = "A data de validade deve estar no formato MMYY")
    private String dataValidade;

    @NotBlank(message = "O CVV é obrigatório")
    @Pattern(regexp = "\\d{3}", message = "O CVV deve conter 3 dígitos")
    private String cvv;

    @NotNull(message = "O tipo do cartão é obrigatório")
    private TipoCartao tipoCartao;

    @NotBlank(message = "O CPF do titular é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos")
    private String cpfTitular;

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long idUsuario;
}