package unifor.pagamento.pagamento.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import unifor.pagamento.pagamento.model.Pagamento;
import unifor.pagamento.pagamento.model.FormaDePagamento;
import unifor.pagamento.pagamento.model.StatusPagamento;
import unifor.pagamento.pagamento.repository.PagamentoRepository;
import unifor.pagamento.pagamento.exception.PagamentoException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarPagamento_DeveCriarNovoPagamento() {
        // Arrange
        BigDecimal valor = new BigDecimal("100.00");
        FormaDePagamento formaPagamento = FormaDePagamento.PIX;
        Long idPedido = 1L;
        String nomeCliente = "João Silva";
        String cpfCliente = "123.456.789-00";

        Pagamento pagamentoEsperado = new Pagamento();
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoEsperado);

        // Act
        Pagamento resultado = pagamentoService.criarPagamento(valor, formaPagamento, idPedido, nomeCliente, cpfCliente, 123L);

        // Assert
        assertNotNull(resultado);
        verify(pagamentoRepository).save(any(Pagamento.class));
    }

    @Test
    void buscarPorId_QuandoPagamentoExiste_DeveRetornarPagamento() {
        // Arrange
        Long id = 1L;
        Pagamento pagamentoEsperado = new Pagamento();
        when(pagamentoRepository.findById(id)).thenReturn(Optional.of(pagamentoEsperado));

        // Act
        Pagamento resultado = pagamentoService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(pagamentoEsperado, resultado);
    }

    @Test
    void buscarPorId_QuandoPagamentoNaoExiste_DeveLancarExcecao() {
        // Arrange
        Long id = 1L;
        when(pagamentoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PagamentoException.class, () -> pagamentoService.buscarPorId(id));
    }

    @Test
    void listarTodos_DeveRetornarListaDePagamentos() {
        // Arrange
        List<Pagamento> pagamentosEsperados = Arrays.asList(new Pagamento(), new Pagamento());
        when(pagamentoRepository.findAll()).thenReturn(pagamentosEsperados);

        // Act
        List<Pagamento> resultado = pagamentoService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void buscarPorStatus_DeveRetornarPagamentosComStatusEspecifico() {
        // Arrange
        StatusPagamento status = StatusPagamento.PENDENTE;
        List<Pagamento> pagamentosEsperados = Arrays.asList(new Pagamento(), new Pagamento());
        when(pagamentoRepository.findByStatus(status)).thenReturn(pagamentosEsperados);

        // Act
        List<Pagamento> resultado = pagamentoService.buscarPorStatus(status);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }
} 