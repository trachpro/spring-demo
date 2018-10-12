package springmvc.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.hateoas.ResourceSupport;

//import java.util.ArrayList;
//import java.util.List;

@Document(collection = "pets")

public class Pet  extends ResourceSupport{

    @Id
    private String _id;

    @Field("name")
    private String name;

    @Field("species")
    private String species;

    @Field("breed")
    private String breed;

    public Pet(String name, String species, String breed){

        this.name = name;
        this.species = species;
        this.breed = breed;
    }

    public String get_id() {
        return _id;
    }

    public void set_id( String _id) {

        this._id = _id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {

        return species;
    }

    public void setSpecies(String species) {

        this.species = species;
    }

    public String getBreed() {

        return breed;
    }

    public void setBreed(String breed) {

        this.breed = breed;
    }
}
