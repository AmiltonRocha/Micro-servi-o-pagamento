package unifor.pagamento.pagamento.exception;
//Classe para tratar exceções globais
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Trata erro quando pagamento não é encontrado
    @ExceptionHandler(PagamentoException.class)
    public ResponseEntity<ErroResponse> handlePagamentoException(PagamentoException ex) {
        ErroResponse erro = new ErroResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(erro);
    }

    // Trata outros erros não esperados
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntimeException(RuntimeException ex) {
        ErroResponse erro = new ErroResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno do servidor",
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    // Classe para formatar a resposta de erro
    private static class ErroResponse {
        private int status;
        private String mensagem;
        private long timestamp;

        public ErroResponse(int status, String mensagem, long timestamp) {
            this.status = status;
            this.mensagem = mensagem;
            this.timestamp = timestamp;
        }

        // Getters
        @SuppressWarnings("unused")
        public int getStatus() { return status; }
        @SuppressWarnings("unused")
        public String getMensagem() { return mensagem; }
        @SuppressWarnings("unused")
        public long getTimestamp() { return timestamp; }
    }
} 