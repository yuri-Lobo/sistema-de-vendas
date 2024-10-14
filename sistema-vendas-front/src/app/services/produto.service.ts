import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Produto } from '../models/produto.model';

@Injectable({
  providedIn: 'root'
})
export class ProdutoService {
  private apiUrl = 'http://localhost:8081/api/produtos';

  constructor(private http: HttpClient) {}

  getProdutos(): Observable<Produto[]> {
    return this.http.get<Produto[]>(this.apiUrl);
  }

  getProdutoById(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${this.apiUrl}/${id}`);
  }

  createProduto(produto: Produto): Observable<Produto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Produto>(this.apiUrl, produto, { headers });
  }

  updateProduto(produto: Produto): Observable<Produto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<Produto>(`${this.apiUrl}/${produto.id}`, produto, { headers });
  }

  deleteProduto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
