package testesigep.exception;

public class ProdutoNotFoundException extends RuntimeException {
    public ProdutoNotFoundException(Integer id) {
        super("Produto não encontrado com o ID: " + id);
    }
}
