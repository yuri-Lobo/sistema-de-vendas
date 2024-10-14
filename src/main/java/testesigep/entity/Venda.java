package testesigep.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Venda {
	
    private Integer id;
    private String cliente; 
    private BigDecimal valorTotal;
    private List<VendaProduto> vendaProdutos;

    public Venda() {
    	this.vendaProdutos = new ArrayList<>();
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

	public List<VendaProduto> getVendaProdutos() {
		return vendaProdutos;
	}

	public void setVendaProdutos(List<VendaProduto> vendaProdutos) {
		this.vendaProdutos = vendaProdutos;
	}
    
}
