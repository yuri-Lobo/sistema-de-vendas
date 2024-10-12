package testesigep.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import testesigep.entity.Produto;

@Repository
public class ProdutoRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Produto produto) {
        String sql = "INSERT INTO produtos (nome, descricao, quantidadeDisponivel, valorUnitario) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, produto.getNome(), produto.getDescricao(), produto.getQuantidadeDisponivel(), produto.getValorUnitario());
    }

    public List<Produto> findAll() {
        String sql = "SELECT * FROM produtos";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Produto produto = new Produto();
            produto.setId((int) rs.getLong("id"));
            produto.setNome(rs.getString("nome"));
            produto.setDescricao(rs.getString("descricao"));
            produto.setQuantidadeDisponivel(rs.getInt("quantidadeDisponivel"));
            produto.setValorUnitario(rs.getBigDecimal("valorUnitario"));
            return produto;
        });
    }
    
    public Produto findById(Integer id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Produto produto = new Produto();
            produto.setId((int) rs.getLong("id"));
            produto.setNome(rs.getString("nome"));
            produto.setDescricao(rs.getString("descricao"));
            produto.setQuantidadeDisponivel(rs.getInt("quantidadeDisponivel"));
            produto.setValorUnitario(rs.getBigDecimal("valorUnitario"));
            return produto;
        });
    }

    public void update(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, quantidadeDisponivel = ?, valorUnitario = ? WHERE id = ?";
        jdbcTemplate.update(sql, produto.getNome(), produto.getDescricao(), produto.getQuantidadeDisponivel(), produto.getValorUnitario(), produto.getId());
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}