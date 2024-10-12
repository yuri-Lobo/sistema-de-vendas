package testesigep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import testesigep.entity.Produto;
import testesigep.entity.Venda;
import testesigep.exception.VendaNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class VendaService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public Venda createVenda(Venda venda) {
	    Produto[] produtos = venda.getProdutos();

	    if (produtos == null || produtos.length == 0) {
	        throw new IllegalArgumentException("A lista de produtos não pode ser nula ou vazia");
	    }

	    List<Integer> quantidades = venda.getQuantidades();
	    if (quantidades == null || quantidades.isEmpty() || quantidades.size() != produtos.length) {
	        throw new IllegalArgumentException("A lista de quantidades deve ter o mesmo tamanho que a lista de produtos e não pode ser nula ou vazia");
	    }

	    for (int i = 0; i < produtos.length; i++) {
	        Produto produto = produtos[i];

	        if (produto == null) {
	            throw new IllegalArgumentException("Produto não pode ser nulo");
	        }

	        System.out.println("Produto ID: " + produto.getId() + 
	                           ", Quantidade Disponível: " + produto.getQuantidadeDisponivel() + 
	                           ", Nome: " + produto.getNome());

	        if (produto.getId() == null) {
	            throw new IllegalArgumentException("ID do produto não pode ser nulo");
	        }

	        if (produto.getQuantidadeDisponivel() == null || produto.getQuantidadeDisponivel() <= 0) {
	            throw new IllegalArgumentException("Quantidade disponível deve ser maior que zero para o produto ID: " + produto.getId());
	        }

	        Integer quantidade = quantidades.get(i);
	        if (quantidade == null || quantidade <= 0) {
	            throw new IllegalArgumentException("Quantidade para o produto ID " + produto.getId() + " deve ser maior que zero");
	        }

	        if (quantidade > produto.getQuantidadeDisponivel()) {
	            throw new IllegalArgumentException("Quantidade para o produto ID " + produto.getId() + " não pode ser maior que a quantidade disponível");
	        }
	    }

	    ObjectMapper objectMapper = new ObjectMapper();
	    ArrayNode arrayNode = objectMapper.createArrayNode();

	    for (int i = 0; i < produtos.length; i++) {
	        ObjectNode objectNode = arrayNode.addObject();
	        objectNode.put("id", produtos[i].getId());
	        objectNode.put("quantidade", quantidades.get(i));
	    }

	    String quantidadesJson;
	    try {
	        quantidadesJson = objectMapper.writeValueAsString(arrayNode);
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Erro ao converter quantidades para JSON", e);
	    }

	    String sql = "INSERT INTO vendas (cliente, valor_total, quantidades) VALUES (?, ?, ?)";
	    KeyHolder keyHolder = new GeneratedKeyHolder();

	    jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, venda.getCliente());
	        ps.setBigDecimal(2, venda.getValorTotal());
	        ps.setString(3, quantidadesJson);
	        return ps;
	    }, keyHolder);

	    Integer id = keyHolder.getKey().intValue();
	    venda.setId(id);
	    saveItensVenda(id, produtos, quantidades);
	    return venda;
	}

	
	private void saveItensVenda(Integer vendaId, Produto[] produtos, List<Integer> quantidades) {
	    if (quantidades == null || quantidades.isEmpty()) {
	        throw new IllegalArgumentException("Quantidades não podem ser nulas ou vazias");
	    }

	    if (produtos == null || produtos.length == 0) {
	        throw new IllegalArgumentException("Produtos não podem ser nulos ou vazios");
	    }

	    if (produtos.length != quantidades.size()) {
	        throw new IllegalArgumentException("O número de quantidades não corresponde ao número de produtos");
	    }

	    StringBuilder produtosQuantidades = new StringBuilder();
	    produtosQuantidades.append("{");
	    for (int i = 0; i < produtos.length; i++) {
	        produtosQuantidades.append("\"id : ")
	                           .append(produtos[i].getId())
	                           .append("\"quantidade : ")
	                           .append(quantidades.get(i));
	        if (i < produtos.length - 1) {
	            produtosQuantidades.append(", ");
	        }
	    }
	    produtosQuantidades.append("}");
	    String sql = "UPDATE vendas SET quantidades = ? WHERE id = ?";
	    jdbcTemplate.update(sql, produtosQuantidades.toString(), vendaId);
	}

	public Venda getVenda(Integer id) {
		String sql = "SELECT * FROM vendas WHERE id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { id }, new VendaRowMapper());
		} catch (EmptyResultDataAccessException e) {
			throw new VendaNotFoundException(id);
		}
	}

	public List<Venda> getAllVendas() {
		String sql = "SELECT * FROM vendas";
		return jdbcTemplate.query(sql, new VendaRowMapper());
	}

	public Venda updateVenda(Integer id, Venda venda) {
		String sql = "UPDATE vendas SET cliente = ?, valor_total = ? WHERE id = ?";
		jdbcTemplate.update(sql, venda.getCliente(), venda.getValorTotal(), id);
		venda.setId(id);
		return venda;
	}

	public boolean deleteVenda(Integer id) {
		String sql = "DELETE FROM vendas WHERE id = ?";
		int rowsAffected = jdbcTemplate.update(sql, id);
		return rowsAffected > 0;
	}

	private static class VendaRowMapper implements RowMapper<Venda> {
		@Override
		public Venda mapRow(ResultSet rs, int rowNum) throws SQLException {
			Venda venda = new Venda();
			venda.setId(rs.getInt("id"));
			venda.setCliente(rs.getString("cliente"));
			venda.setValorTotal(rs.getBigDecimal("valor_total"));
			return venda;
		}
	}
}
