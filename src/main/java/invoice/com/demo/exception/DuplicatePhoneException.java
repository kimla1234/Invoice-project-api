package invoice.com.demo.exception;

public class DuplicatePhoneException extends RuntimeException{
    public DuplicatePhoneException (String message){
        super(message);
    }
}
