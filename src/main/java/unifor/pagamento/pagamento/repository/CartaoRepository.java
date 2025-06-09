package unifor.pagamento.pagamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unifor.pagamento.pagamento.model.Cartao;
import unifor.pagamento.pagamento.model.TipoCartao;

import java.util.List;
import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    List<Cartao> findByCpfTitular(String cpfTitular);
    List<Cartao> findByTipoCartao(TipoCartao tipoCartao);
    Optional<Cartao> findByNumeroCartao(String numeroCartao);
    List<Cartao> findByIdUsuario(Long idUsuario);
} 