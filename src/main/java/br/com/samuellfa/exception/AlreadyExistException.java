package br.com.samuellfa.exception;

import io.grpc.Status;

public class AlreadyExistException extends BaseBusinessException {

    public static final String ERROR_MESSAGE = "%s already exist.";
    public final String name;

    public AlreadyExistException(String name) {
        super(String.format(ERROR_MESSAGE, name));
        this.name = name;
    }

    @Override
    public Status getStatusCode() {
        return Status.ALREADY_EXISTS;
    }

    @Override
    public String getErrorMessage() {
        return String.format(ERROR_MESSAGE, this.name);
    }
}
