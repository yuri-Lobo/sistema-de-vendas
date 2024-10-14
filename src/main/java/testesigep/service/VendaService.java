package testesigep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import testesigep.entity.Produto;
import testesigep.entity.Venda;
import testesigep.entity.VendaProduto;
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

    @Autowired
    private VendaProdutoService vendaProdutoService;

    public Venda createVenda(Venda venda, Produto[] produtos, List<Integer> quantidades) {
    	String sqlVenda = "INSERT INTO vendas (cliente, valor_total) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, venda.getCliente()); 
            ps.setBigDecimal(2, venda.getValorTotal()); 
            return ps;
        }, keyHolder);

        Long vendaId = keyHolder.getKey().longValue();
        venda.setId(vendaId.intValue());

        for (int i = 0; i < produtos.length; i++) {
            Produto produto = produtos[i];
            int quantidade = quantidades.get(i);
            String sqlVendaProduto = "INSERT INTO venda_produto (venda_id, produto_id, quantidade) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlVendaProduto, vendaId, produto.getId(), quantidade);
        }

        return venda; 
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
