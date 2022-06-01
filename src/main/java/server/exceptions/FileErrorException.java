package server.exceptions;

public class FileErrorException extends RuntimeException{
    public FileErrorException(String message) {
        super(message);
    }
}
