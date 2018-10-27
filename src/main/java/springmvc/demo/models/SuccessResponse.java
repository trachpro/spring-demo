package springmvc.demo.models;

import org.json.JSONObject;

public class SuccessResponse {

    private String message;

    private JSONObject data;

    public SuccessResponse(String message, JSONObject data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
