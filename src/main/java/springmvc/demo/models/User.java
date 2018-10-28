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
public class User  extends Person {

    public User(){}

    public User(String name, String email) {
        super(name, email);
    }

    public User(String name, String password, String email){
        super(name, password, email);
    }
}
