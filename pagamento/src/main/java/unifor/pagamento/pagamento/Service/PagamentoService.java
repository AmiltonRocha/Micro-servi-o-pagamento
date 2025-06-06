package unifor.pagamento.pagamento.Service;
//implementação de regras de negocios
import org.springframework.beans.factory.annotation.Autowired; //No nosso caso, vai injetar o PagamentoRepository no Service
import unifor.pagamento.pagamento.model.Pagamento;//Contém todos os dados e comportamentos de um pagamento
import unifor.pagamento.pagamento.model.StatusPagamento;//Contém todos os estados possíveis de um pagamento
import unifor.pagamento.pagamento.model.FormaDePagamento;//Contém todos os tipos de pagamento possíveis
import unifor.pagamento.pagamento.repository.PagamentoRepository;//Contém todos os métodos de acesso ao banco de dados
import unifor.pagamento.pagamento.exception.PagamentoException;//Nossa exceção personalizada
import java.math.BigDecimal;//Contém todos os métodos matemáticos
import java.util.List;//Contém todos os métodos de lista
import org.springframework.stereotype.Service;//Indica que esta classe contém regras de negócio

@Service//Indica que a classe é um Service
public class PagamentoService {
    @Autowired //Injetar o PagamentoRepository no Service
    private PagamentoRepository pagamentoRepository;

    // Método para criar um novo pagamento
    public Pagamento criarPagamento(BigDecimal valor, FormaDePagamento formaPagamento, 
                                  Long idPedido, String nomeCliente, String cpfCliente, Long idUsuario) {
        try {
            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new PagamentoException("Valor deve ser maior que zero");
            }
            if (formaPagamento == null) {
                throw new PagamentoException("Forma de pagamento é obrigatória");
            }
            if (idPedido == null || idPedido <= 0) {
                throw new PagamentoException("ID do pedido é obrigatório");
            }
            if (nomeCliente == null || nomeCliente.trim().isEmpty()) {
                throw new PagamentoException("Nome do cliente é obrigatório");
            }
            if (cpfCliente == null || cpfCliente.trim().isEmpty()) {
                throw new PagamentoException("CPF do cliente é obrigatório");
            }

            Pagamento pagamento = new Pagamento();
            pagamento.setValor(valor);
            pagamento.setFormaPagamento(formaPagamento);
            pagamento.setIdPedido(idPedido);
            pagamento.setNomeCliente(nomeCliente);
            pagamento.setCpfCliente(cpfCliente);
            pagamento.setIdUsuario(idUsuario);
            pagamento.setStatus(StatusPagamento.PENDENTE);
            return pagamentoRepository.save(pagamento);
        } catch (Exception e) {
            throw new PagamentoException("Erro ao criar pagamento: " + e.getMessage());
        }
    }

    // Método para buscar um pagamento por ID
    public Pagamento buscarPorId(Long id) {
        return pagamentoRepository.findById(id)
            .orElseThrow(() -> new PagamentoException("Pagamento não encontrado com ID: " + id));
    }

    // Método para listar todos os pagamentos
    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    // Método para buscar pagamentos por status
    public List<Pagamento> buscarPorStatus(StatusPagamento status) {
        return pagamentoRepository.findByStatus(status);
    }

    // Método para buscar pagamentos por CPF do cliente
    public List<Pagamento> buscarPorCpfCliente(String cpfCliente) {
        return pagamentoRepository.findByCpfCliente(cpfCliente);
    }

    // Método para buscar pagamentos por ID do pedido
    public List<Pagamento> buscarPorIdPedido(Long idPedido) {
        return pagamentoRepository.findByIdPedido(idPedido);
    }

    // Método para buscar pagamentos por ID do usuário
    public List<Pagamento> buscarPorIdUsuario(Long idUsuario) {
        return pagamentoRepository.findByIdUsuario(idUsuario);
    }

    // Método para aprovar um pagamento
    public Pagamento aprovarPagamento(Long id) {
        Pagamento pagamento = buscarPorId(id);
        try {
            pagamento.aprovarPagamento();
            return pagamentoRepository.save(pagamento);
        } catch (IllegalStateException e) {
            throw new PagamentoException("Não é possível aprovar o pagamento: " + e.getMessage());
        }
    }

    // Método para rejeitar um pagamento
    public Pagamento rejeitarPagamento(Long id) {
        Pagamento pagamento = buscarPorId(id);
        try {
            pagamento.rejeitarPagamento();
            return pagamentoRepository.save(pagamento);
        } catch (IllegalStateException e) {
            throw new PagamentoException("Não é possível rejeitar o pagamento: " + e.getMessage());
        }
    }

    // Método para cancelar um pagamento
    public Pagamento cancelarPagamento(Long id) {
        Pagamento pagamento = buscarPorId(id);
        try {
            pagamento.cancelarPagamento();
            return pagamentoRepository.save(pagamento);
        } catch (IllegalStateException e) {
            throw new PagamentoException("Não é possível cancelar o pagamento: " + e.getMessage());
        }
    }

    // Método para deletar um pagamento
    public void deletarPagamento(Long id) {
        try {
            Pagamento pagamento = buscarPorId(id);
            pagamentoRepository.delete(pagamento);
        } catch (Exception e) {
            throw new PagamentoException("Erro ao deletar pagamento: " + e.getMessage());
        }
    }
}

