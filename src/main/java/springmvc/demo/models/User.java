package springmvc.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.hateoas.ResourceSupport;

//import java.util.ArrayList;
//import java.util.List;

@Document(collection = "users")

public class User  extends ResourceSupport{

    @Id
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

    public User(String name, String password, String email, String role, String address){

        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.address = address;
        isConfirmed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

}
