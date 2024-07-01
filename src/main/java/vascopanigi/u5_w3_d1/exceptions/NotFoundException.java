package vascopanigi.u5_w3_d1.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("Record with id: " + id + ", not found!");
    }
    public NotFoundException(String str) {
        super("Record with id: " + str + ", not found!");
    }
}