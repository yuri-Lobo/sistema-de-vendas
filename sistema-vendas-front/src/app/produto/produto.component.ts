import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Produto } from '../models/produto.model';
import { ProdutoService } from '../services/produto.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; // Import do CommonModule
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-produto',
  templateUrl: './produto.component.html',
  styleUrls: ['./produto.component.css'],
  standalone: true,
  providers: [ProdutoService],
  imports: [
    MatTableModule,
    ReactiveFormsModule,
    FormsModule,
    CommonModule, // Certifique-se de que o CommonModule está aqui
    HttpClientModule
  ]
})
export class ProdutoComponent implements OnInit {
  displayedColumns: string[] = ['nome', 'descricao', 'quantidadeDisponivel', 'valorUnitario', 'actions'];
  dataSource = new MatTableDataSource<Produto>([]);
  produto: Produto = { nome: '', descricao: '', quantidadeDisponivel: 0, valorUnitario: 0 };
  editMode = false;

  constructor(private produtoService: ProdutoService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.carregarProdutos();
  }

  carregarProdutos(): void {
    this.produtoService.getProdutos().subscribe(
      (data: Produto[]) => {
        console.log('Produtos carregados:', data);
        this.dataSource.data = data; // Define diretamente a dataSource
        this.cdr.detectChanges();
      },
      (error) => {
        console.error('Erro ao carregar produtos:', error);
      }
    );
}

  addProduto(): void {
    this.produtoService.createProduto(this.produto).subscribe(newProduto => {
      this.dataSource.data = [...this.dataSource.data, newProduto];
      this.resetForm();
    });
  }

  updateProduto(): void {
    this.produtoService.updateProduto(this.produto).subscribe(updatedProduto => {
      const index = this.dataSource.data.findIndex(p => p.id === updatedProduto.id);
      if (index !== -1) {
        this.dataSource.data[index] = updatedProduto;
        this.dataSource.data = [...this.dataSource.data]; 
        console.log('dataSource após atualizar produto:', this.dataSource.data);
      }
      this.resetForm();
    });
  }

  editProduto(produto: Produto): void {
    this.produto = { ...produto };
    this.editMode = true;
  }

  deleteProduto(id: number): void {
    this.produtoService.deleteProduto(id).subscribe(() => {
      this.dataSource.data = this.dataSource.data.filter(p => p.id !== id);
      this.dataSource.data = [...this.dataSource.data]; 
    });
  }

  resetForm(): void {
    this.produto = { nome: '', descricao: '', quantidadeDisponivel: 0, valorUnitario: 0 };
    this.editMode = false;
  }
}
