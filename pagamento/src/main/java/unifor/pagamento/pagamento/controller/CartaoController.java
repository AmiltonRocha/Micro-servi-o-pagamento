package unifor.pagamento.pagamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unifor.pagamento.pagamento.model.Cartao;
import unifor.pagamento.pagamento.repository.CartaoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cartoes")
@CrossOrigin(origins = "http://localhost:3000")
public class CartaoController {

    @Autowired
    private CartaoRepository cartaoRepository;

    // Criar novo cartão
    @PostMapping
    public ResponseEntity<Cartao> criarCartao(@RequestBody Cartao cartao) {
        if (cartao.getIdUsuario() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Cartao novoCartao = cartaoRepository.save(cartao);
            return ResponseEntity.ok(novoCartao);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Buscar todos os cartões
    @GetMapping
    public ResponseEntity<List<Cartao>> listarTodos() {
        List<Cartao> cartoes = cartaoRepository.findAll();
        return ResponseEntity.ok(cartoes);
    }

    // Buscar cartão por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cartao> buscarPorId(@PathVariable Long id) {
        Optional<Cartao> cartao = cartaoRepository.findById(id);
        return cartao.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar por CPF do titular
    @GetMapping("/cpf/{cpfTitular}")
    public ResponseEntity<List<Cartao>> buscarPorCpfTitular(@PathVariable String cpfTitular) {
        List<Cartao> cartoes = cartaoRepository.findByCpfTitular(cpfTitular);
        return ResponseEntity.ok(cartoes);
    }

    // Buscar por ID do usuário
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Cartao>> buscarPorIdUsuario(@PathVariable Long idUsuario) {
        List<Cartao> cartoes = cartaoRepository.findByIdUsuario(idUsuario);
        return ResponseEntity.ok(cartoes);
    }

    // Atualizar cartão
    @PutMapping("/{id}")
    public ResponseEntity<Cartao> atualizarCartao(@PathVariable Long id, @RequestBody Cartao cartaoAtualizado) {
        return cartaoRepository.findById(id)
                .map(_ -> {
                    cartaoAtualizado.setId(id);
                    return ResponseEntity.ok(cartaoRepository.save(cartaoAtualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar cartão
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartao(@PathVariable Long id) {
        return cartaoRepository.findById(id)
                .map(cartao -> {
                    cartaoRepository.delete(cartao);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 