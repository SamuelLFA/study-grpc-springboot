package br.com.samuellfa.exception;

import io.grpc.Status;

public class NotFoundException extends BaseBusinessException {

    public static final String ERROR_MESSAGE = "%d does not exist.";
    public final Long id;

    public NotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
        this.id = id;
    }

    @Override
    public Status getStatusCode() {
        return Status.NOT_FOUND;
    }

    @Override
    public String getErrorMessage() {
        return String.format(ERROR_MESSAGE, this.id);
    }
}
