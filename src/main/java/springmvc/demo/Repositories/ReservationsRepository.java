package springmvc.demo.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.models.Reservation;

@Repository
@Transactional
public interface ReservationsRepository extends MongoRepository<Reservation, String> {
    //    public void deleteByName
    public Reservation findReservationBy_id(String id);
    public Long deleteUserBy_id(String id);
}
