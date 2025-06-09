package unifor.pagamento.pagamento.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unifor.pagamento.pagamento.dto.CartaoDTO;
import unifor.pagamento.pagamento.model.Cartao;
import unifor.pagamento.pagamento.repository.CartaoRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cartoes")
@CrossOrigin(origins = "http://localhost:5173") // Ajuste a origem conforme seu frontend
public class CartaoController {

    @Autowired
    private CartaoRepository cartaoRepository;

    // --- MÉTODOS DE CONVERSÃO ---
    private CartaoDTO toDTO(Cartao cartao) {
        CartaoDTO dto = new CartaoDTO();
        dto.setId(cartao.getId());
        dto.setNumeroCartao("**** **** **** " + cartao.getNumeroCartao().substring(12)); // Mascarando o número
        dto.setNomeTitular(cartao.getNomeTitular());
        dto.setDataValidade(cartao.getDataValidade());
        dto.setTipoCartao(cartao.getTipoCartao());
        dto.setCpfTitular(cartao.getCpfTitular());
        dto.setIdUsuario(cartao.getIdUsuario());
        // CVV nunca é retornado
        return dto;
    }

    private Cartao toEntity(CartaoDTO dto) {
        Cartao cartao = new Cartao();
        cartao.setId(dto.getId()); // O ID será nulo na criação
        cartao.setNumeroCartao(dto.getNumeroCartao());
        cartao.setNomeTitular(dto.getNomeTitular());
        cartao.setDataValidade(dto.getDataValidade());
        cartao.setCvv(dto.getCvv());
        cartao.setTipoCartao(dto.getTipoCartao());
        cartao.setCpfTitular(dto.getCpfTitular());
        cartao.setIdUsuario(dto.getIdUsuario());
        return cartao;
    }

    // --- ENDPOINTS ---

    @PostMapping
    public ResponseEntity<CartaoDTO> criarCartao(@Valid @RequestBody CartaoDTO cartaoDTO) {
        // Garante que não está tentando criar um cartão com um ID existente
        cartaoDTO.setId(null);
        Cartao cartao = toEntity(cartaoDTO);
        Cartao novoCartao = cartaoRepository.save(cartao);
        return new ResponseEntity<>(toDTO(novoCartao), HttpStatus.CREATED);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<CartaoDTO>> buscarPorIdUsuario(@PathVariable Long idUsuario) {
        List<CartaoDTO> cartoes = cartaoRepository.findByIdUsuario(idUsuario).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cartoes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartao(@PathVariable Long id) {
        if (!cartaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cartaoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}