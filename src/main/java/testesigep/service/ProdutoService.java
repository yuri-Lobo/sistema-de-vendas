package testesigep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import testesigep.entity.Produto;
import testesigep.exception.ProdutoNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Produto createProduto(Produto produto) {
    	Integer quantidadeDisponivel = produto.getQuantidadeDisponivel();
    	if (quantidadeDisponivel == null) {
            throw new IllegalArgumentException("Quantidade disponível não pode ser nula");
        }
        String sql = "INSERT INTO produtos (nome, descricao, quantidade_disponivel, valor_unitario) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getDescricao());
            ps.setInt(3, produto.getQuantidadeDisponivel());
            ps.setBigDecimal(4, produto.getValorUnitario());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        produto.setId(id.intValue());
        return produto;
    }

    public Produto getProduto(Integer id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new ProdutoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new ProdutoNotFoundException(id); 
        }
    }

    public List<Produto> getAllProdutos() {
        String sql = "SELECT * FROM produtos";
        return jdbcTemplate.query(sql, new ProdutoRowMapper());
    }

    public Produto updateProduto(Long id, Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, quantidade_disponivel = ?, valor_unitario = ? WHERE id = ?";
        jdbcTemplate.update(sql, produto.getNome(), produto.getDescricao(), produto.getQuantidadeDisponivel(), produto.getValorUnitario(), id);
        produto.setId(id.intValue()); 
        return produto;
    }

    public boolean deleteProduto(Long id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0; 
    }

    private static class ProdutoRowMapper implements RowMapper<Produto> {
        @Override
        public Produto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Produto produto = new Produto();
            produto.setId((int) rs.getLong("id"));
            produto.setNome(rs.getString("nome"));
            produto.setDescricao(rs.getString("descricao"));
            produto.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));
            produto.setValorUnitario(rs.getBigDecimal("valor_unitario"));
            return produto;
        }
    }

}
