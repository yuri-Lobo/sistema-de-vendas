package testesigep.exception;

public class VendaNotFoundException extends RuntimeException {
    public VendaNotFoundException(Integer id) {
        super("Venda não encontrado com o ID: " + id);
    }
}
