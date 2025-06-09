package unifor.pagamento.pagamento.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import unifor.pagamento.pagamento.dto.AgendamentoResponseDTO;
import unifor.pagamento.pagamento.dto.CarrinhoResponseDTO;
import unifor.pagamento.pagamento.dto.ClienteDTO;
import unifor.pagamento.pagamento.exception.PagamentoException;
import unifor.pagamento.pagamento.model.FormaDePagamento;
import unifor.pagamento.pagamento.model.Pagamento;
import unifor.pagamento.pagamento.model.StatusPagamento;
import unifor.pagamento.pagamento.repository.PagamentoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

    private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);
    private final PagamentoRepository pagamentoRepository;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public PagamentoService(PagamentoRepository pagamentoRepository, WebClient.Builder webClientBuilder) {
        this.pagamentoRepository = pagamentoRepository;
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * MÉTODO DE CHECKOUT ATUALIZADO
     * A assinatura agora só precisa do idCliente e idUsuario, buscando os outros dados.
     */
    public Pagamento processarCheckoutCliente(Long idCliente, Long idUsuario) {
        logger.info("Iniciando processamento de checkout para cliente ID: {} e usuário ID: {}", idCliente, idUsuario);
        
        try {
            // 1. Busca os detalhes do cliente no conta-service para obter nome e CPF.
            logger.info("Buscando dados do cliente no serviço CONTA para ID: {}", idCliente);
            ClienteDTO cliente = webClientBuilder.build().get()
                    .uri("http://CONTA/api/contas/clientes/{idCliente}", idCliente)
                    .retrieve()
                    .bodyToMono(ClienteDTO.class)
                    .block();

            if (cliente == null || cliente.getCpf() == null) {
                logger.error("Cliente não encontrado ou inválido para ID: {}", idCliente);
                throw new PagamentoException("Cliente com ID " + idCliente + " não encontrado ou inválido.");
            }
            logger.info("Cliente encontrado: {} (CPF: {})", cliente.getNome(), cliente.getCpf());

            // 2. Busca débitos pendentes de outros serviços.
            logger.info("Buscando total de vendas para cliente ID: {}", idCliente);
            BigDecimal totalVendas = buscarTotalVendas(idCliente);
            logger.info("Total de vendas encontrado: R$ {}", totalVendas);

            logger.info("Buscando total de serviços para cliente ID: {}", idCliente);
            BigDecimal totalServicos = buscarTotalServicos(idCliente);
            logger.info("Total de serviços encontrado: R$ {}", totalServicos);

            // 3. Soma os totais e cria o registro de pagamento.
            BigDecimal valorTotal = totalVendas.add(totalServicos);
            logger.info("Valor total do checkout para o cliente {}: R$ {}", cliente.getNome(), valorTotal);

            if (valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
                logger.warn("Nenhum valor pendente encontrado para o cliente ID: {}", idCliente);
                throw new PagamentoException("Nenhum valor pendente encontrado para o cliente.");
            }

            // Usa os dados do cliente que foram buscados para criar o pagamento.
            logger.info("Criando pagamento para cliente {} com valor total de R$ {}", cliente.getNome(), valorTotal);
            return this.criarPagamento(valorTotal, FormaDePagamento.PIX, System.currentTimeMillis(), cliente.getNome(), cliente.getCpf(), idUsuario);
        } catch (Exception e) {
            logger.error("Erro ao processar checkout para cliente ID: {} - Erro: {}", idCliente, e.getMessage(), e);
            throw new PagamentoException("Erro ao processar checkout: " + e.getMessage());
        }
    }

    private BigDecimal buscarTotalVendas(Long idCliente) {
        try {
            logger.info("Iniciando busca de vendas no serviço VENDAS-SERVICE para cliente ID: {}", idCliente);
            CarrinhoResponseDTO carrinhoResponse = webClientBuilder.build().get()
                .uri("http://VENDAS-SERVICE/carrinho/{idCliente}", idCliente)
                .retrieve()
                .bodyToMono(CarrinhoResponseDTO.class).block();
            
            if (carrinhoResponse == null) {
                logger.warn("Resposta nula do serviço VENDAS-SERVICE para cliente ID: {}", idCliente);
                return BigDecimal.ZERO;
            }
            
            if (!carrinhoResponse.isSuccess()) {
                logger.warn("Resposta não bem-sucedida do serviço VENDAS-SERVICE para cliente ID: {} - Success: {}", 
                    idCliente, carrinhoResponse.isSuccess());
                return BigDecimal.ZERO;
            }
            
            if (carrinhoResponse.getData() == null) {
                logger.warn("Dados nulos na resposta do serviço VENDAS-SERVICE para cliente ID: {}", idCliente);
                return BigDecimal.ZERO;
            }
            
            if (carrinhoResponse.getData().getTotal() == null) {
                logger.warn("Total nulo na resposta do serviço VENDAS-SERVICE para cliente ID: {}", idCliente);
                return BigDecimal.ZERO;
            }
            
            logger.info("Total do carrinho encontrado para cliente ID {}: R$ {}", 
                idCliente, carrinhoResponse.getData().getTotal());
            return carrinhoResponse.getData().getTotal();
        } catch (Exception e) {
            logger.error("Erro ao buscar carrinho de compras do vendas-service para cliente ID: {} - Erro: {}", 
                idCliente, e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal buscarTotalServicos(Long idCliente) {
        try {
            AgendamentoResponseDTO[] agendamentos = webClientBuilder.build().get()
                .uri("http://AGENDAMENTO/agendamentos/cliente/{idCliente}/pendentes", idCliente)
                .retrieve()
                .bodyToMono(AgendamentoResponseDTO[].class)
                .onErrorResume(e -> Mono.empty())
                .block();
            if (agendamentos != null) {
                BigDecimal total = BigDecimal.ZERO;
                for (AgendamentoResponseDTO agendamento : agendamentos) {
                    if (agendamento.getValorServico() != null) {
                        total = total.add(agendamento.getValorServico());
                    }
                }
                logger.info("Total de serviços encontrado: R$ {}", total);
                return total;
            }
        } catch (Exception e) {
            logger.warn("AVISO: Nao foi possivel buscar agendamentos pendentes do servicos-service: {}", e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    // --- SEUS MÉTODOS ORIGINAIS (Mantidos e Corrigidos) ---

    public Pagamento criarPagamento(BigDecimal valor, FormaDePagamento formaPagamento, Long idPedido, String nomeCliente, String cpfCliente, Long idUsuario) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) throw new PagamentoException("Valor deve ser maior que zero");
        if (idUsuario == null) throw new PagamentoException("ID do usuário é obrigatório");
        if (formaPagamento == null) throw new PagamentoException("Forma de pagamento é obrigatória");

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(valor);
        pagamento.setFormaPagamento(formaPagamento);
        pagamento.setIdPedido(idPedido);
        pagamento.setNomeCliente(nomeCliente);
        pagamento.setCpfCliente(cpfCliente);
        pagamento.setIdUsuario(idUsuario);
        pagamento.setStatus(StatusPagamento.PENDENTE);
        
        // CORREÇÃO FINAL: Define a data e hora atuais para evitar o erro de coluna nula
        pagamento.setDataPagamento(LocalDateTime.now()); 

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento buscarPorId(Long id) {
        return pagamentoRepository.findById(id)
            .orElseThrow(() -> new PagamentoException("Pagamento não encontrado com ID: " + id));
    }

    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public List<Pagamento> buscarPorStatus(StatusPagamento status) {
        return pagamentoRepository.findByStatus(status);
    }

    public List<Pagamento> buscarPorCpfCliente(String cpfCliente) {
        return pagamentoRepository.findByCpfCliente(cpfCliente);
    }

    public List<Pagamento> buscarPorIdPedido(Long idPedido) {
        return pagamentoRepository.findByIdPedido(idPedido);
    }

    public List<Pagamento> buscarPorIdUsuario(Long idUsuario) {
        return pagamentoRepository.findByIdUsuario(idUsuario);
    }

    public Pagamento aprovarPagamento(Long id) {
        Pagamento pagamento = buscarPorId(id);
        pagamento.aprovarPagamento();
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento rejeitarPagamento(Long id) {
        Pagamento pagamento = buscarPorId(id);
        pagamento.rejeitarPagamento();
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento cancelarPagamento(Long id) {
        Pagamento pagamento = buscarPorId(id);
        pagamento.cancelarPagamento();
        return pagamentoRepository.save(pagamento);
    }

    public void deletarPagamento(Long id) {
        Pagamento pagamento = buscarPorId(id);
        pagamentoRepository.delete(pagamento);
    }
}