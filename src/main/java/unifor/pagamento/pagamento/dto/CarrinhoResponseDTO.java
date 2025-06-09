package unifor.pagamento.pagamento.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora outros campos que não precisamos
public class CarrinhoResponseDTO {

    // CAMPO ADICIONADO QUE ESTAVA FALTANDO
    private boolean success;

    // O campo 'data' na resposta do Node.js contém o objeto do carrinho
    private CarrinhoData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CarrinhoData {
        private BigDecimal total;
        // Outros campos do carrinho são ignorados, pois só precisamos do total.
    }
}