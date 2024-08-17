package Volt.example.Volt.Shared;

public class ServiceResponse<T> extends ServiceResponses {
    private T Data;

    public ServiceResponse(T data, Boolean success, String message) {
        super(success, message);
        Data = data;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}

class ServiceResponses {
    private Boolean success = true;
    private String message = null;

    public ServiceResponses(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}