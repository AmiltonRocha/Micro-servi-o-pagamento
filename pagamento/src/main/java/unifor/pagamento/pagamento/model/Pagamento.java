package unifor.pagamento.pagamento.model;
import jakarta.persistence.*;// Anotações do JPA
import lombok.Data;// Anotação do Lombok para criar getters e setters
import lombok.NoArgsConstructor;// Anotação do Lombok para criar construtor sem argumentos
import java.math.BigDecimal;// Classe para representar números decimais
import java.time.LocalDateTime;// Classe para representar datas e horas

@Entity// Anotação para indicar que a classe é uma entidade JPA
@Table(name = "Petshop_pagamentos")// Anotação para especificar o nome da tabela no banco de dados
@Data// Anotação do Lombok para criar getters e setters
@NoArgsConstructor// Anotação do Lombok para criar construtor sem argumentos
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal valor;
    
    @Column(nullable = false)
    private LocalDateTime dataPagamento;
    
    @Enumerated(EnumType.STRING)//indica que o enum será armazenado como String no banco de dados
    @Column(nullable = false)
    private StatusPagamento status;
    
    @Enumerated(EnumType.STRING)//indica que o enum será armazenado como String no banco de dados
    @Column(nullable = false)
    private FormaDePagamento formaPagamento;
    
    @Column(nullable = false)
    private Long idPedido;
    
    @Column(nullable = false)
    private String nomeCliente;
    
    @Column(nullable = false)
    private String cpfCliente;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    // Método para processar um novo pagamento
    public void processarPagamento(BigDecimal valor, FormaDePagamento formaPagamento, Long idPedido, String nomeCliente, String cpfCliente) {
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.idPedido = idPedido;
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.dataPagamento = LocalDateTime.now();
        this.status = StatusPagamento.PENDENTE;
    }

    // Método para atualizar o status do pagamento
    public void atualizarStatus(StatusPagamento novoStatus) {
        if (this.status == StatusPagamento.CANCELADO) {
            throw new IllegalStateException("Não é possível alterar o status de um pagamento cancelado"); //Não é possível alterar o status de um pagamento cancelado
        }
        
        if (this.status == StatusPagamento.APROVADO && novoStatus != StatusPagamento.CANCELADO) {
            throw new IllegalStateException("Não é possível alterar o status de um pagamento aprovado"); //Não é possível alterar o status de um pagamento aprovado
        }
        
        this.status = novoStatus;
    }

    // Método para aprovar o pagamento
    public void aprovarPagamento() {
        if (this.status != StatusPagamento.PENDENTE) {
            throw new IllegalStateException("Apenas pagamentos pendentes podem ser aprovados");
        }
        this.status = StatusPagamento.APROVADO;
    }

    // Método para rejeitar o pagamento
    public void rejeitarPagamento() {
        if (this.status != StatusPagamento.PENDENTE) {
            throw new IllegalStateException("Apenas pagamentos pendentes podem ser rejeitados");
        }
        this.status = StatusPagamento.REJEITADO;
    }

    // Método para cancelar o pagamento
    public void cancelarPagamento() {
        if (this.status == StatusPagamento.CANCELADO) {
            throw new IllegalStateException("Este pagamento já está cancelado");
        }
        if (this.status == StatusPagamento.REJEITADO) {
            throw new IllegalStateException("Não é possível cancelar um pagamento rejeitado");
        }
        this.status = StatusPagamento.CANCELADO;
    }

    // Método para verificar se o pagamento está pendente
    public boolean isPendente() {
        return this.status == StatusPagamento.PENDENTE;
    }

    // Método para verificar se o pagamento está aprovado
    public boolean isAprovado() {
        return this.status == StatusPagamento.APROVADO;
    }

    // Método para verificar se o pagamento está rejeitado
    public boolean isRejeitado() {
        return this.status == StatusPagamento.REJEITADO;
    }

    // Método para verificar se o pagamento está cancelado
    public boolean isCancelado() {
        return this.status == StatusPagamento.CANCELADO;
    }
}

