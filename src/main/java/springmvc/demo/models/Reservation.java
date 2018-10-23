package springmvc.demo.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

//import javax.persistence.ManyToOne;

@Document(collection = "Reservations")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Reservation {

    @Id
    private String _id;

//    @ManyToOne(mappedBy = "reservations")
    @Field("user_id")
    private String user_id;

    public Reservation() {}

    public Reservation(String _id, String user_id){

        this._id = _id;
        this.user_id = user_id;
    }

    @JsonProperty("_id")
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }
}
