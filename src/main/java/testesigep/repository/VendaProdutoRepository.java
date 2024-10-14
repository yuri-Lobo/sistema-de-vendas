package testesigep.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import testesigep.entity.VendaProduto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VendaProdutoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public VendaProduto save(VendaProduto vendaProduto) {
        String sql = "INSERT INTO VendaProduto (venda_id, produto_id, quantidade) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, vendaProduto.getVendaId(), vendaProduto.getProdutoId(), vendaProduto.getQuantidade());
        return vendaProduto;
    }

    public VendaProduto findById(Integer vendaId, Integer produtoId) {
        String sql = "SELECT * FROM VendaProduto WHERE venda_id = ? AND produto_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{vendaId, produtoId}, new VendaProdutoRowMapper());
    }

    public List<VendaProduto> findByVendaId(Integer vendaId) {
        String sql = "SELECT * FROM VendaProduto WHERE venda_id = ?";
        return jdbcTemplate.query(sql, new Object[]{vendaId}, new VendaProdutoRowMapper());
    }

    public boolean delete(Integer vendaId, Integer produtoId) {
        String sql = "DELETE FROM VendaProduto WHERE venda_id = ? AND produto_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, vendaId, produtoId);
        return rowsAffected > 0;
    }

    private static class VendaProdutoRowMapper implements RowMapper<VendaProduto> {
        @Override
        public VendaProduto mapRow(ResultSet rs, int rowNum) throws SQLException {
            VendaProduto vendaProduto = new VendaProduto();
            vendaProduto.setVendaId(rs.getInt("venda_id"));
            vendaProduto.setProdutoId(rs.getInt("produto_id"));
            vendaProduto.setQuantidade(rs.getInt("quantidade"));
            return vendaProduto;
        }
    }
}
