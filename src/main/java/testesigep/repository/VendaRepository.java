package testesigep.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import testesigep.entity.Venda;
import testesigep.exception.VendaNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VendaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Venda save(Venda venda) {
        String sql = "INSERT INTO vendas (cliente, valor_total) VALUES (?, ?)";
        jdbcTemplate.update(sql, venda.getCliente(), venda.getValorTotal());
        return venda;
    }

    public Venda findById(Integer id) {
        String sql = "SELECT * FROM vendas WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new VendaRowMapper());
        } catch (VendaNotFoundException e) {
            throw new VendaNotFoundException(id);
        }
    }

    public List<Venda> findAll() {
        String sql = "SELECT * FROM vendas";
        return jdbcTemplate.query(sql, new VendaRowMapper());
    }

    public void update(Integer id, Venda venda) {
        String sql = "UPDATE vendas SET cliente = ?, valor_total = ? WHERE id = ?";
        jdbcTemplate.update(sql, venda.getCliente(), venda.getValorTotal(), id);
    }

    public boolean delete(Integer id) {
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
