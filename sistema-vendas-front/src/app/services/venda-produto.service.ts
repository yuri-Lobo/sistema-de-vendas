import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VendaProduto } from '../models/venda-produto.model';

@Injectable({
  providedIn: 'root' 
})
export class VendaProdutoService {
  private apiUrl = 'http://localhost:8081/api/venda-produtos'; 

  constructor(private http: HttpClient) {}

  createVendaProduto(vendaProduto: VendaProduto): Observable<VendaProduto> {
    return this.http.post<VendaProduto>(this.apiUrl, vendaProduto);
  }

  getVendaProdutos(vendaId: number): Observable<VendaProduto[]> {
    return this.http.get<VendaProduto[]>(`${this.apiUrl}/venda/${vendaId}`);
  }

  deleteVendaProduto(vendaId: number, produtoId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${vendaId}/${produtoId}`);
  }
}
