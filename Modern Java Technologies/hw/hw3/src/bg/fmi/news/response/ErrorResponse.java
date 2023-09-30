package bg.fmi.news.response;

public final class ErrorResponse {

    private String status;
    private String code;
    private String message;

    private ErrorResponse() {
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
