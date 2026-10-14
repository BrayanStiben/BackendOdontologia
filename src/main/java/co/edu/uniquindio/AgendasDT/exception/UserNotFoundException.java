package co.edu.uniquindio.AgendasDT.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() { super("El usuario no existe."); }
}
