package vascopanigi.u5_w3_d1.exceptions;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException {
    private List<ObjectError> errorList;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(List<ObjectError> errorList) {
        super("There were errors with payload validation.");
        this.errorList = errorList;
    }
}
