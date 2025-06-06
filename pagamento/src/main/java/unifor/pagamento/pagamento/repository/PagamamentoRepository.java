package unifor.pagamento.pagamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unifor.pagamento.pagamento.model.Pagamento;
import unifor.pagamento.pagamento.model.StatusPagamento;
import java.util.List;

@Repository
public interface PagamamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByStatus(StatusPagamento status);
    List<Pagamento> findByCpfCliente(String cpfCliente);
    List<Pagamento> findByIdPedido(Long idPedido);
} 