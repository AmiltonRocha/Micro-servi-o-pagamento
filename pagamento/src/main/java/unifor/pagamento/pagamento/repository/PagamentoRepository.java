package unifor.pagamento.pagamento.repository;
// esse e o CRUD para o banco de dados
// JpaRepository é uma interface que fornece métodos para operações CRUD básicas
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Anotação para indicar que a classe é um repositório
import unifor.pagamento.pagamento.model.Pagamento;
import unifor.pagamento.pagamento.model.StatusPagamento;
import java.util.List;


@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    // Busca pagamentos por status
    List<Pagamento> findByStatus(StatusPagamento status);
    
    // Busca pagamentos por CPF do cliente
    List<Pagamento> findByCpfCliente(String cpfCliente);
    
    // Busca pagamentos por ID do pedido
    List<Pagamento> findByIdPedido(Long idPedido);

    // Busca pagamentos por ID do usuário
    List<Pagamento> findByIdUsuario(Long idUsuario);
}
