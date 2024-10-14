package testesigep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import testesigep.dto.VendaProdutoDTO;
import testesigep.entity.Produto;
import testesigep.entity.Venda;
import testesigep.entity.VendaProduto;
import testesigep.exception.ProdutoNotFoundException;
import testesigep.exception.VendaNotFoundException;
import testesigep.service.ProdutoService;
import testesigep.service.VendaService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<?> createVenda(@RequestBody VendaProdutoDTO vendaRequest) {
        if (vendaRequest.getVenda() == null || vendaRequest.getVendaProdutos() == null) {
            return ResponseEntity.badRequest().body("Venda ou lista de produtos não pode ser nula."); // 400 Bad Request
        }

        Venda venda = vendaRequest.getVenda();
        List<VendaProduto> vendaProdutos = vendaRequest.getVendaProdutos();

        if (vendaProdutos.isEmpty()) {
            return ResponseEntity.badRequest().body("A lista de produtos não pode ser vazia."); // 400 Bad Request
        }

        Produto[] produtos = new Produto[vendaProdutos.size()];
        List<Integer> quantidades = new ArrayList<>();

        for (int i = 0; i < vendaProdutos.size(); i++) {
            VendaProduto vp = vendaProdutos.get(i);
            if (vp.getProdutoId() == null) {
                return ResponseEntity.badRequest().body("ID do produto não pode ser nulo."); // 400 Bad Request
            }

            Produto produto;
            try {
                produto = produtoService.getProduto(vp.getProdutoId());
            } catch (ProdutoNotFoundException e) {
                return ResponseEntity.badRequest().body("Produto não encontrado com o ID: " + vp.getProdutoId()); // 400 Bad Request
            }

            produtos[i] = produto;
            quantidades.add(vp.getQuantidade());
        }

        Venda createdVenda = vendaService.createVenda(venda, produtos, quantidades);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVenda);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVenda(@PathVariable Integer id) {
        try {
            Venda venda = vendaService.getVenda(id);
            return ResponseEntity.ok(venda);
        } catch (VendaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada com o ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<Venda>> getAllVendas() {
        List<Venda> vendas = vendaService.getAllVendas();
        return ResponseEntity.ok(vendas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVenda(@PathVariable Integer id, @RequestBody Venda venda) {
        try {
            Venda updatedVenda = vendaService.updateVenda(id, venda);
            return ResponseEntity.ok(updatedVenda);
        } catch (VendaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada com o ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenda(@PathVariable Integer id) {
        if (vendaService.deleteVenda(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
