package testesigep.dbconfig;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DbConfiguration implements CommandLineRunner {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:sistemavendas;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("admin");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) throws Exception {
        JdbcTemplate jdbcTemplate = jdbcTemplate(dataSource());

        String createProdutosTable = "CREATE TABLE IF NOT EXISTS produtos (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(255)," +
                "descricao VARCHAR(255)," +
                "quantidade_disponivel INT," +
                "valor_unitario DECIMAL(10, 2)" +
                ")";

        jdbcTemplate.execute(createProdutosTable);

        String createVendasTable = "CREATE TABLE IF NOT EXISTS vendas (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "cliente VARCHAR(255) NOT NULL," +
                "valor_total DECIMAL(10, 2) NOT NULL" +
                ")";

        jdbcTemplate.execute(createVendasTable);

        String createVendaProdutoTable = "CREATE TABLE IF NOT EXISTS venda_produto (" +
                "venda_id BIGINT NOT NULL," +
                "produto_id BIGINT NOT NULL," +
                "quantidade INT NOT NULL," +
                "PRIMARY KEY (venda_id, produto_id)," +
                "FOREIGN KEY (venda_id) REFERENCES vendas(id) ON DELETE CASCADE," +
                "FOREIGN KEY (produto_id) REFERENCES produtos(id)" +
                ")";

        jdbcTemplate.execute(createVendaProdutoTable);
    }
}
