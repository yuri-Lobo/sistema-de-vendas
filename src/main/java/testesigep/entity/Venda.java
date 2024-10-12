package testesigep.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private Integer id;
    private String cliente; 
    private BigDecimal valorTotal;
    private Produto[] produtos; 
    private List<Integer> quantidades; 

    public Venda() {
        this.quantidades = new ArrayList<>(); 
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Produto[] getProdutos() {
        return produtos != null ? produtos : new Produto[0];
    }

    public void setProdutos(Produto[] produtos) {
        this.produtos = produtos;
    }

    public List<Integer> getQuantidades() {
        return quantidades != null ? quantidades : new ArrayList<>(); 
    }

    public void setQuantidades(List<Integer> quantidades) {
        this.quantidades = quantidades != null ? quantidades : new ArrayList<>(); 
    }
}
