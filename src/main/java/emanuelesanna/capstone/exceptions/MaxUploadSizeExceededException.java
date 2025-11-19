package emanuelesanna.capstone.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class MaxUploadSizeExceededException extends RuntimeException {
    private List<String> errorsMessages;

    public MaxUploadSizeExceededException(List<String> errorsMessages) {
        super("Ci sono i seguenti errori di validazione: ");
        this.errorsMessages = errorsMessages;
    }
}
