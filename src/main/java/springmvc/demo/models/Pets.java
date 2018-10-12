package springmvc.demo.models;

import java.util.ArrayList;
import java.util.List;

public class Pets {

    private List<Pet> list;
    private static Pets instance = null;
//    private ArrayList vailua;

    public static Pets getInstance() {
        if(instance == null) {
            instance = new Pets();
        }

        return instance;
    }

    public Pets() {
        list = new ArrayList<Pet>();

        list.add(new Pet("name", "species", "breed"));
    }

    public List<Pet> getList() {

        return list;
    }

    public Pet getPetByName(String name) {

        for(Pet p : list) {

            if(p.getName() == name) {

                return p;
            }
        }

        return null;
    }

    private boolean checkInclude(String name) {

        for(Pet i : list) {

            if(i.getName().equals(name)) {

                return true;
            }
        }

        return false;
    }

    public boolean createPet(String name, String species, String breed) {

        if(!checkInclude(name)) return false;

        Pet p = new Pet(name, species, breed);

        list.add(p);

        return true;
    }
}
