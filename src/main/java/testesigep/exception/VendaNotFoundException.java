package testesigep.exception;

public class VendaNotFoundException extends RuntimeException {
    
    public VendaNotFoundException(Integer id) {
        super("Venda não encontrada com o ID: " + id);
    }
    
    public VendaNotFoundException(String message) {
        super(message);
    }
    
    public VendaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public VendaNotFoundException(Throwable cause) {
        super(cause);
    }
}
