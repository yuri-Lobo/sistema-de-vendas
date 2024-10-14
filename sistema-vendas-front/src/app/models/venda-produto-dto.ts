import { VendaProduto } from "./venda-produto.model";

export interface VendaProdutoDTO {
    venda: { id?: number; cliente: string; valorTotal: number; }; // Aqui id pode ser opcional
    vendaProdutos: VendaProduto[];
  }
  