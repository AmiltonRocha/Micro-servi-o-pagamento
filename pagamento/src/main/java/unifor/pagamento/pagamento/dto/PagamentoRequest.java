package unifor.pagamento.pagamento.dto;
// fazer a requisição do pagamento para formato json

import java.math.BigDecimal;
import unifor.pagamento.pagamento.model.FormaDePagamento;

public class PagamentoRequest {
    private BigDecimal valor;
    private FormaDePagamento formaPagamento;
    private Long idPedido;
    private String nomeCliente;
    private String cpfCliente;

    // Getters e setters
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public FormaDePagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaDePagamento formaPagamento) { this.formaPagamento = formaPagamento; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getCpfCliente() { return cpfCliente; }
    public void setCpfCliente(String cpfCliente) { this.cpfCliente = cpfCliente; }
} 