import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Venda } from '../models/venda.model'; 
import { VendaProdutoDTO } from '../models/venda-produto-dto';
import { VendaProduto } from '../models/venda-produto.model';

@Injectable({
  providedIn: 'root' 
})
export class VendaService {
  private apiUrl = 'http://localhost:8081/api/vendas'; // Corrigido para usar apiUrl
  private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) {}

  private handleError(operation: string) {
    return (error: any): Observable<never> => {
      console.error(`Erro em ${operation}:`, error);
      return throwError(`Erro em ${operation}, tente novamente mais tarde.`);
    };
  }

  getVendas(): Observable<Venda[]> {
    return this.http.get<Venda[]>(this.apiUrl).pipe(
      catchError(this.handleError('obter vendas'))
    );
  }

  getVendaById(id: number): Observable<Venda> {
    return this.http.get<Venda>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError('obter venda'))
    );
  }

  createVenda(vendaRequest: VendaProdutoDTO): Observable<Venda> {
    console.log('Dados enviados para a API:', vendaRequest); // Considerar remover em produção
    return this.http.post<Venda>(this.apiUrl, vendaRequest, { headers: this.headers }).pipe(
      catchError(this.handleError('criar venda'))
    );
  }

  getProdutosVenda(vendaId: number): Observable<VendaProduto[]> {
    return this.http.get<VendaProduto[]>(`${this.apiUrl}/${vendaId}/produtos`).pipe(
      catchError(this.handleError('obter produtos da venda'))
    );
  }

  updateVenda(vendaDTO: VendaProdutoDTO): Observable<Venda> {
    // Corrigido: usou apiUrl ao invés de baseUrl
    return this.http.put<Venda>(`${this.apiUrl}/${vendaDTO.venda.id}`, vendaDTO, { headers: this.headers }).pipe(
      catchError(this.handleError('atualizar venda')) // Adicionando tratamento de erro
    );
  }

  deleteVenda(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError('excluir venda'))
    );
  }
}
