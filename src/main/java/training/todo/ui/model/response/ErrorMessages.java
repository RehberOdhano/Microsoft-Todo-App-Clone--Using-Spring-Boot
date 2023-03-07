package training.todo.ui.model.response;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field!"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Record not found"),
    AUTHENTICATION_FAILED("Authentication failed!"),
    COULD_NOT_UPDATE_RECORD("Couldn't update the record"),
    COULD_NOT_DELETE_RECORD("Couldn't delete the record"),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address couldn't be verified"),
    INVALID_CREDENTIALS("username or password is incorrect!");

    private String errorMessage;
    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
