package springmvc.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

public class Revenue {


    @Id
    @Field("_id")
    @JsonIgnore
    private String _id;

    @Field("month")
    @JsonIgnore
    private int month;

    @Field("year")
    @JsonIgnore
    private int year;

    @Field("revenue")
    private long revenue;

    @Field("numberOfRooms")
    private int numberOfRooms;

    private String time;

    public Revenue(String _id, int month, int year, int numberOfRooms) {
        this._id = _id;
        this.month = month;
        this.year = year;
        this.numberOfRooms = numberOfRooms;
        this.time = Integer.toString(month) + "-" + Integer.toString(year);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
}
