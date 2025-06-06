package unifor.pagamento.pagamento.controller;
import org.springframework.beans.factory.annotation.Autowired;  // Para injeção de dependência
import org.springframework.http.ResponseEntity;            // Para retornar respostas HTTP
import org.springframework.web.bind.annotation.*;          // Para anotações de endpoints (@GetMapping, @PostMapping, etc)
import unifor.pagamento.pagamento.model.Pagamento;        // Nossa classe de pagamento
import unifor.pagamento.pagamento.model.StatusPagamento;  // Enum de status
// Enum de forma de pagamento
import unifor.pagamento.pagamento.Service.PagamentoService;  // Nosso service que criamos
  // Para valores monetários
import java.util.List;        // Para listas de pagamentos
import unifor.pagamento.pagamento.dto.PagamentoRequest;
import unifor.pagamento.pagamento.dto.PagamentoAcaoRequest;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {
    
    @Autowired
    private PagamentoService pagamentoService;
   
    //  criar um novo pagamento
    @PostMapping
    public ResponseEntity<Pagamento> criarPagamento(@RequestBody Pagamento pagamento) {
        if (pagamento.getIdUsuario() == null) {
            return ResponseEntity.badRequest().build();
        }
        Pagamento novoPagamento = pagamentoService.criarPagamento(
            pagamento.getValor(),
            pagamento.getFormaPagamento(),
            pagamento.getIdPedido(),
            pagamento.getNomeCliente(),
            pagamento.getCpfCliente(),
            pagamento.getIdUsuario()
        );
        return ResponseEntity.ok(novoPagamento);
    }

    // Buscar um pagamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Long id) {
        Pagamento pagamento = pagamentoService.buscarPorId(id);
        return ResponseEntity.ok(pagamento); //.ok caso ele encontre o pagamento, ele retorna o pagamento.
    }

    // Listar todos os pagamentos
    @GetMapping
    public ResponseEntity<List<Pagamento>> listarTodos() {
        List<Pagamento> pagamentos = pagamentoService.listarTodos();
        return ResponseEntity.ok(pagamentos);
    }

    // Buscar pagamentos por status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pagamento>> buscarPorStatus(@PathVariable StatusPagamento status) {
        List<Pagamento> pagamentos = pagamentoService.buscarPorStatus(status);
        return ResponseEntity.ok(pagamentos);
        //Pegando a palavra "PENDENTE" que está no endereço
       // Colocando essa palavra na variável status
      // O Spring converte automaticamente "PENDENTE" para o enum StatusPagamento
        
    }

    // Buscar pagamentos por CPF do cliente
    @GetMapping("/cliente/{cpf}")
    public ResponseEntity<List<Pagamento>> buscarPorCpfCliente(@PathVariable String cpf) {
        List<Pagamento> pagamentos = pagamentoService.buscarPorCpfCliente(cpf);
        return ResponseEntity.ok(pagamentos);
    }

    // Aprovar um pagamento
    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Pagamento> aprovarPagamento(@PathVariable Long id, @RequestBody(required = false) PagamentoAcaoRequest request) {
        Pagamento pagamento = pagamentoService.aprovarPagamento(id);
        return ResponseEntity.ok(pagamento);
    }

    // Rejeitar um pagamento
    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<Pagamento> rejeitarPagamento(@PathVariable Long id, @RequestBody(required = false) PagamentoAcaoRequest request) {
        Pagamento pagamento = pagamentoService.rejeitarPagamento(id);
        return ResponseEntity.ok(pagamento);
    }

    // Cancelar um pagamento
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Pagamento> cancelarPagamento(@PathVariable Long id, @RequestBody(required = false) PagamentoAcaoRequest request) {
        Pagamento pagamento = pagamentoService.cancelarPagamento(id);
        return ResponseEntity.ok(pagamento);
    }

    // Deletar um pagamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPagamento(@PathVariable Long id) {
        pagamentoService.deletarPagamento(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/pedido/{idPedido}")
    public List<Pagamento> buscarPorIdPedido(@PathVariable Long idPedido) {
        return pagamentoService.buscarPorIdPedido(idPedido);
    }

    // Classe para representar erros
    private static class ErrorResponse {
        private int status;
        private String mensagem;
        private long timestamp;

        public ErrorResponse(int status, String mensagem) {
            this.status = status;
            this.mensagem = mensagem;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters e Setters
        @SuppressWarnings("unused")
        public int getStatus() { return status; }
        @SuppressWarnings("unused")
        public void setStatus(int status) { this.status = status; }
        @SuppressWarnings("unused")
        public String getMensagem() { return mensagem; }
        @SuppressWarnings("unused")
        public void setMensagem(String mensagem) { this.mensagem = mensagem; }
        @SuppressWarnings("unused")
        public long getTimestamp() { return timestamp; }
        @SuppressWarnings("unused")
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Pagamento>> buscarPorIdUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pagamentoService.buscarPorIdUsuario(idUsuario));
    }
}
