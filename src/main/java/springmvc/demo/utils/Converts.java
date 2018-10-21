package springmvc.demo.utils;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class Converts {

    public static JSONObject convertModelToJson(Object object) {

        System.out.println(object.getClass());

        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        try {
            JSONObject request = new JSONObject(jsonString);

            return request;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
