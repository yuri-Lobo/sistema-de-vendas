import { Produto } from './produto.model'; // Importe a interface Produto
import { VendaProduto } from './venda-produto.model';

export interface Venda {
  id?: number;
  cliente: string;
  valorTotal: number;
  produtos?: VendaProduto[]; // Adicionando produtos como um array de VendaProduto
}
