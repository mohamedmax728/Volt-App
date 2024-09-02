package Volt.example.Volt.Shared;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;


@Getter
@Setter
@NoArgsConstructor
public class ServiceResponse<T> extends ServiceResponses {
    private T Data;
    private HttpStatusCode httpStatusCode;

    public ServiceResponse(T data, boolean success, String enMsg, String arMsg
    , HttpStatusCode httpStatusCode) {
        super(success, enMsg, arMsg, httpStatusCode);
        Data = data;
        this.httpStatusCode = httpStatusCode;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}

@RequiredArgsConstructor
@Getter
class ServiceResponses {
    private boolean success = true;
    private String enMsg = null;
    private String arMsg = null;
    private HttpStatusCode httpStatusCode;
    public ServiceResponses(boolean success, String enMsg, String arMsg, HttpStatusCode httpStatusCode) {
        this.enMsg = enMsg;
        this.arMsg = arMsg;
        this.success = success;
        this.httpStatusCode = httpStatusCode;
    }


}