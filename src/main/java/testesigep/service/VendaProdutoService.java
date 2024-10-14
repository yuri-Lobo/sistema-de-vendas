package testesigep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import testesigep.entity.VendaProduto;
import testesigep.exception.VendaNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class VendaProdutoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public VendaProduto createVendaProduto(VendaProduto vendaProduto) {
        String sql = "INSERT INTO venda_produto (venda_id, produto_id, quantidade) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, vendaProduto.getVendaId());
            ps.setInt(2, vendaProduto.getProdutoId());
            ps.setInt(3, vendaProduto.getQuantidade());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        vendaProduto.setId(id.intValue()); 
        return vendaProduto; 
    }

    public VendaProduto getVendaProduto(Integer vendaId, Integer produtoId) {
        String sql = "SELECT * FROM venda_produto WHERE venda_id = ? AND produto_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{vendaId, produtoId}, new VendaProdutoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new VendaNotFoundException("Item de venda não encontrado para a venda ID " + vendaId + " e produto ID " + produtoId);
        }
    }

    public List<VendaProduto> getAllVendaProdutos(Integer vendaId) {
        String sql = "SELECT * FROM venda_produto WHERE venda_id = ?";
        return jdbcTemplate.query(sql, new Object[]{vendaId}, new VendaProdutoRowMapper());
    }

    public boolean deleteVendaProduto(Integer vendaId, Integer produtoId) {
        String sql = "DELETE FROM venda_produto WHERE venda_id = ? AND produto_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, vendaId, produtoId);
        return rowsAffected > 0; 
    }

    private static class VendaProdutoRowMapper implements RowMapper<VendaProduto> {
        @Override
        public VendaProduto mapRow(ResultSet rs, int rowNum) throws SQLException {
            VendaProduto vendaProduto = new VendaProduto();
            vendaProduto.setId(rs.getInt("id")); 
            vendaProduto.setVendaId(rs.getInt("venda_id")); 
            vendaProduto.setProdutoId(rs.getInt("produto_id")); 
            vendaProduto.setQuantidade(rs.getInt("quantidade")); 
            return vendaProduto; 
        }
    }
}
