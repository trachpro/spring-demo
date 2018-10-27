package springmvc.demo.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.models.Room;

@Repository
@Transactional
public interface RoomsRepository extends MongoRepository<Room, String> {
}
