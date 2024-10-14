package testesigep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testesigep.entity.VendaProduto;
import testesigep.exception.VendaNotFoundException;
import testesigep.service.VendaProdutoService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/vendas/{vendaId}/produtos")
public class VendaProdutoController {

    @Autowired
    private VendaProdutoService vendaProdutoService;

    @PostMapping
    public ResponseEntity<VendaProduto> createVendaProduto(@PathVariable Integer vendaId, @RequestBody VendaProduto vendaProduto) {
        vendaProduto.setVendaId(vendaId);
        VendaProduto createdVendaProduto = vendaProdutoService.createVendaProduto(vendaProduto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVendaProduto);
    }

    @GetMapping
    public ResponseEntity<List<VendaProduto>> getAllVendaProdutos(@PathVariable Integer vendaId) {
        List<VendaProduto> vendaProdutos = vendaProdutoService.getAllVendaProdutos(vendaId);
        return ResponseEntity.ok(vendaProdutos);
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<VendaProduto> getVendaProduto(@PathVariable Integer vendaId, @PathVariable Integer produtoId) {
        try {
            VendaProduto vendaProduto = vendaProdutoService.getVendaProduto(vendaId, produtoId);
            return ResponseEntity.ok(vendaProduto);
        } catch (VendaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> deleteVendaProduto(@PathVariable Integer vendaId, @PathVariable Integer produtoId) {
        if (vendaProdutoService.deleteVendaProduto(vendaId, produtoId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
