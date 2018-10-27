package springmvc.demo.models;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "rooms")
public class Room {

    @Id
    private String _id;

    @Field(value = "capacity")
    private int capacity;

    @Field(value = "price")
    private int price;

    @Field(value = "type")
    private String type;

    @Field(value = "status")
    private String status;

    @Field(value = "beds")
    private Map<String, Integer> beds;

    @Field(value="roomNo")
    private String roomNo;

    public Room() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Integer> getBeds() {
        return beds;
    }

    public void setBeds(Map beds) {
        this.beds = beds;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
