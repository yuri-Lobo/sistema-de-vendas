import { Component, OnInit } from '@angular/core';
import { VendaProduto } from '../models/venda-produto.model';
import { VendaProdutoService } from '../services/venda-produto.service';

@Component({
  selector: 'app-venda-produto',
  templateUrl: './venda-produto.component.html',
  styleUrls: ['./venda-produto.component.css'],
  standalone: true,
  imports: [/* adicione outros módulos que você precisar, como FormsModule se usar formulários */]
})
export class VendaProdutoComponent implements OnInit {
  vendaProdutos: VendaProduto[] = [];
  vendaProduto: VendaProduto = { vendaId: 0, produtoId: 0, quantidade: 1 };

  constructor(private vendaProdutoService: VendaProdutoService) {}

  ngOnInit(): void {
    this.loadVendaProdutos();
  }

  loadVendaProdutos(): void {
  }

  addVendaProduto(): void {
    this.vendaProdutoService.createVendaProduto(this.vendaProduto).subscribe(newVendaProduto => {
      this.vendaProdutos.push(newVendaProduto);
      this.resetForm();
    });
  }

  deleteVendaProduto(vendaId: number, produtoId: number): void {
    this.vendaProdutoService.deleteVendaProduto(vendaId, produtoId).subscribe(() => {
      this.vendaProdutos = this.vendaProdutos.filter(vp => vp.vendaId !== vendaId && vp.produtoId !== produtoId);
    });
  }

  resetForm(): void {
    this.vendaProduto = { vendaId: 0, produtoId: 0, quantidade: 1 };
  }
}
