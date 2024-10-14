import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Venda } from '../models/venda.model';
import { Produto } from '../models/produto.model'; 
import { VendaService } from '../services/venda.service';
import { ProdutoService } from '../services/produto.service'; 
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { VendaProduto } from '../models/venda-produto.model';
import { VendaProdutoDTO } from '../models/venda-produto-dto';

@Component({
  selector: 'app-venda',
  templateUrl: './venda.component.html',
  styleUrls: ['./venda.component.css'],
  standalone: true,
  imports: [
    MatTableModule,
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    RouterModule 
  ]
})
export class VendaComponent implements OnInit {
  vendas: Venda[] = [];
  produtos: Produto[] = []; 
  displayedColumns: string[] = ['cliente', 'valorTotal', 'actions']; 
  dataSource = new MatTableDataSource<Venda>([]);
  venda: Venda = { cliente: '', valorTotal: 0 };
  produtosVenda: { produto: Produto; quantidade: number }[] = []; 
  editMode = false;
  selectedProduto!: Produto; 
  quantidade: number = 0; 

  constructor(
    private vendaService: VendaService,
    private produtoService: ProdutoService 
  ) {}

  ngOnInit() {
    this.loadVendas(); 
    this.loadProdutos(); 
  }

  loadVendas() {
    this.vendaService.getVendas().subscribe({
      next: (data) => {
        this.vendas = data; 
        this.dataSource.data = data;
      },
      error: (error) => {
        console.error('Erro ao carregar vendas:', error);
      }
    });
  }

  addVenda(): void {
    if (this.produtosVenda.length === 0) {
        alert('Adicione pelo menos um produto à venda!');
        return;
    }

    const produtosParaVenda: VendaProduto[] = this.produtosVenda.map(item => ({
        vendaId: 0, // Para criação de nova venda, você pode usar 0 ou undefined
        produtoId: item.produto.id!, // Atributo correto
        quantidade: item.quantidade
    }));

    const vendaDTO: VendaProdutoDTO = {
        venda: {
            cliente: this.venda.cliente,
            valorTotal: this.venda.valorTotal
        },
        vendaProdutos: produtosParaVenda
    };

    console.log('Dados da nova venda:', vendaDTO);
    this.vendaService.createVenda(vendaDTO).subscribe({
        next: (newVenda) => {
            this.dataSource.data.push(newVenda);
            this.dataSource._updateChangeSubscription();
            this.resetForm();
        },
        error: (error) => {
            console.error('Erro ao criar venda:', error);
        }
    });
    window.location.reload();
  }

  addProdutoVenda(): void {
    if (this.quantidade > 0 && this.selectedProduto) {
      const produtoVenda = { produto: this.selectedProduto, quantidade: this.quantidade };
      this.produtosVenda.push(produtoVenda);
      this.updateValorTotal();
      console.log('Produto adicionado à venda:', produtoVenda);
      this.resetProdutoSelecao();
    } else {
      alert('Selecione um produto e insira uma quantidade válida.');
    }
  }

  updateValorTotal(): void {
    this.venda.valorTotal = this.produtosVenda.reduce((total, item) => {
        return total + (item.produto.valorUnitario * item.quantidade);
    }, 0);
    console.log('Valor total atualizado:', this.venda.valorTotal);
  }

  loadProdutos() {
    this.produtoService.getProdutos().subscribe({
      next: (data) => {
        this.produtos = data;
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
      }
    });
  }

  updateVenda(): void {
    if (this.produtosVenda.length === 0) {
        alert('Adicione pelo menos um produto à venda!');
        return;
    }

    if (!this.venda.id) {
        alert('ID da venda não está definido.');
        return;
    }

    const produtosParaVenda: VendaProduto[] = this.produtosVenda.map(item => {
      if (!item.produto.id) {
        alert('ID do produto não está definido.');
        return null;
      }

      if (item.quantidade <= 0) {
        alert('A quantidade deve ser maior que zero.');
        return null;
      }

      return {
        vendaId: this.venda.id!, // Usando a propriedade correta
        produtoId: item.produto.id, // Usando a propriedade correta
        quantidade: item.quantidade 
      };
    }).filter(item => item !== null) as VendaProduto[];

    if (produtosParaVenda.length === 0) {
        alert('Nenhum produto válido para venda.');
        return;
    }

    const vendaDTO: VendaProdutoDTO = {
        venda: {
            cliente: this.venda.cliente,
            valorTotal: this.venda.valorTotal
        },
        vendaProdutos: produtosParaVenda
    };

    console.log('Dados da venda antes da atualização:', vendaDTO);

    this.vendaService.updateVenda(vendaDTO).subscribe({
        next: (updatedVenda) => {
            const index = this.dataSource.data.findIndex(v => v.id === updatedVenda.id);
            if (index !== -1) {
                this.dataSource.data[index] = updatedVenda; 
                this.dataSource._updateChangeSubscription();
            }
            this.resetForm();
        },
        error: (error) => {
            console.error('Erro ao atualizar venda:', error);
            alert('Erro ao atualizar venda. Tente novamente mais tarde.');
        }
    });
  }

  editVenda(venda: Venda): void {
    this.venda = { ...venda }; 
    this.produtosVenda = []; 

    if (venda.id) {
      this.loadProdutosVenda(venda.id); 
    } else {
      console.error('ID da venda não está definido.');
    }

    this.editMode = true; 
  }

  deleteVenda(id: number): void {
    if (confirm('Tem certeza que deseja excluir esta venda?')) {
      this.vendaService.deleteVenda(id).subscribe({
        next: () => {
          this.loadVendas(); 
        },
        error: (error) => {
          console.error('Erro ao excluir venda:', error);
        }
      });
    }
  }

  resetForm(): void {
    this.venda = { cliente: '', valorTotal: 0 };
    this.produtosVenda = []; 
    this.quantidade = 0; 
    this.editMode = false;
  }

  resetProdutoSelecao(): void {
    this.selectedProduto = {} as Produto; 
    this.quantidade = 0; 
  }

  removeProdutoVenda(index: number): void {
    if (index > -1) {
      this.produtosVenda.splice(index, 1); // Remove o produto da lista
      this.updateValorTotal(); // Atualiza o valor total
    }
  }

  loadProdutosVenda(vendaId: number): void {
    this.vendaService.getProdutosVenda(vendaId).subscribe({
      next: (produtosVenda: VendaProduto[]) => {
        this.produtosVenda = produtosVenda.map(item => ({
          produto: { id: item.produtoId } as Produto, 
          quantidade: item.quantidade
        }));
        this.updateValorTotal();
      },
      error: (error: any) => {
        console.error('Erro ao carregar produtos da venda:', error);
      }
    });
  }
}
