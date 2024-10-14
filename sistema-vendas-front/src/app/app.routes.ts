import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProdutoComponent } from './produto/produto.component'; // ajuste o caminho conforme necessário
import { VendaProdutoComponent } from './venda-produto/venda-produto.component'; // ajuste o caminho conforme necessário
import { VendaComponent } from './venda/venda.component';

export const routes: Routes = [
  { path: 'vendas', component: VendaComponent },
  { path: 'produtos', component: ProdutoComponent },
  { path: 'venda-produto', component: VendaProdutoComponent },
  { path: '', redirectTo: '/vendas', pathMatch: 'full' }, // redireciona para 'vendas' na raiz
  { path: '**', redirectTo: '/vendas' } // redireciona para 'vendas' para qualquer rota não encontrada
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
