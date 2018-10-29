package springmvc.demo.utils;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static Date convertStringToDate(String sDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(sDate);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
