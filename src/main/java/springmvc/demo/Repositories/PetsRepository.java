package springmvc.demo.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import springmvc.demo.models.Pet;

@Repository
public interface PetsRepository extends MongoRepository<Pet, String> {
//    public void deleteByName
    public Pet findPetByName(String name);
}
