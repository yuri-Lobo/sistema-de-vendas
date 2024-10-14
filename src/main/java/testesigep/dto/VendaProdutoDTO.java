package testesigep.dto;

import testesigep.entity.Venda;
import testesigep.entity.VendaProduto;

import java.util.List;

public class VendaProdutoDTO {
    private Venda venda;
    private List<VendaProduto> vendaProdutos;

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public List<VendaProduto> getVendaProdutos() {
        return vendaProdutos;
    }

    public void setVendaProdutos(List<VendaProduto> vendaProdutos) {
        this.vendaProdutos = vendaProdutos;
    }
}
