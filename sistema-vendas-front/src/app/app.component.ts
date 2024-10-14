import { Component } from '@angular/core';
import { ProdutoComponent } from './produto/produto.component';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  template: `
    <nav>
    <a href="/vendas">Vendas</a>
<a href="/produtos">Produtos</a>

    </nav>
    <router-outlet></router-outlet>
  `,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: true,
  imports: [ProdutoComponent, CommonModule, RouterOutlet]
})
export class AppComponent {
  title = 'Teste SIGEP sistema-vendas';
  
}
