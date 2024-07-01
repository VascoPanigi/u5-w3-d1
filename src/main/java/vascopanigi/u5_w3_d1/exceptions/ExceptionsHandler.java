package vascopanigi.u5_w3_d1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vascopanigi.u5_w3_d1.payloads.ErrorsDTO;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleBadRequest(BadRequestException exception) {
        if (exception.getErrorList() != null) {
            String message = exception.getErrorList().stream().map(objectError -> objectError.getDefaultMessage()).collect(Collectors.joining(". "));
            return new ErrorsDTO(message, LocalDateTime.now());
        } else {
            return new ErrorsDTO(exception.getMessage(), LocalDateTime.now());
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFound(NotFoundException exception) {
        return new ErrorsDTO(exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleGenericErrors(Exception exception) {
        exception.printStackTrace();
        return new ErrorsDTO("There's a problem with the server, go insult the backend devs!", LocalDateTime.now());
    }
}
