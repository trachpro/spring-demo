package springmvc.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "staffs")
public class Staff extends Person {

    @Field("role")
    private String role;

    public Staff(){}

    public Staff(String name, String password, String email, String role) {

        super(name, password, email);

        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
