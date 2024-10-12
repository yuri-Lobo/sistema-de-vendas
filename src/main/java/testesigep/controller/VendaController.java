package testesigep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testesigep.entity.Venda;
import testesigep.service.VendaService;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    public ResponseEntity<Venda> createVenda(@RequestBody Venda venda) {
        Venda createdVenda = vendaService.createVenda(venda);
        return ResponseEntity.status(201).body(createdVenda);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> getVenda(@PathVariable Integer id) {
        Venda venda = vendaService.getVenda(id);
        return ResponseEntity.ok(venda);
    }

    @GetMapping
    public ResponseEntity<List<Venda>> getAllVendas() {
        List<Venda> vendas = vendaService.getAllVendas();
        return ResponseEntity.ok(vendas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venda> updateVenda(@PathVariable Integer id, @RequestBody Venda venda) {
        Venda updatedVenda = vendaService.updateVenda(id, venda);
        return ResponseEntity.ok(updatedVenda);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenda(@PathVariable Integer id) {
        vendaService.deleteVenda(id);
        return ResponseEntity.noContent().build();
    }
}
