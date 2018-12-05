package springmvc.demo.repositories.rooms;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.models.Room;

import java.util.List;

@Repository
@Transactional
public interface RoomsRepository extends MongoRepository<Room, String> {
    public List<Room> findRoomsByCapacity(int capacity);
    public Room findRoomsByRoomNo(int roomNo);
}
