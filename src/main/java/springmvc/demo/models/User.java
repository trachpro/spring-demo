package springmvc.demo.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kaiso.relmongo.annotation.CascadeType;
import io.github.kaiso.relmongo.annotation.FetchType;
import io.github.kaiso.relmongo.annotation.JoinProperty;
import io.github.kaiso.relmongo.annotation.OneToMany;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.hateoas.ResourceSupport;

//import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//import java.util.ArrayList;
//import java.util.List;

@Document(collection = "users")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class User  extends ResourceSupport{

    @Id
    @Field("_id")
    private String _id;

    @Field("name")
    private String name;

    @Field("password")
    private String password;

    @Field("email")
    private String email;

    @Field("role")
    private String role;

    @Field("address")
    private String address;

    @Field("profileImg")
    private String profileImg;

    @Field("isConfirmed")
    private Boolean isConfirmed;

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @Field("reservations")
    private List<Reservation> reservations;


    public User(){}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String password, String email, String role, String address){

        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.address = address;
        isConfirmed = false;
    }

    @JsonProperty("_id")
    public String get_id() {
        return this._id;
    }

    public void set_id(String Id) {
        this._id = Id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("profileImg")
    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    @JsonProperty("isConfirmed")
    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {

        this.reservations = reservations;
    }
}
