package springmvc.demo.models;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springmvc.demo.utils.Converts;

public class ResponseModel {

    private Object data;

    private String message;

    private HttpStatus status;

    public ResponseModel() {}

    public ResponseModel(Object data, String message, HttpStatus status) {

        this.data = data;
        this.message = message;
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public ResponseEntity<String> toResponse() {

        JSONObject result = Converts.convertModelToJson(data);
        result.put("message", message);

        return new ResponseEntity<>(result.toString(),status);
    }
}
