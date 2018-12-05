package springmvc.demo.repositories.reservations;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.models.Reservation;

import java.util.List;

@Repository
@Transactional
public interface ReservationsRepository extends MongoRepository<Reservation, String>, ReservationsRepositoryCustom {
    //    public void deleteByName
    public Reservation findReservationBy_id(String id);
    public Reservation findReservationByCode(String code);
    public Long deleteUserBy_id(String id);
    public List<Reservation> findReservationsByCustomerEmail(String email);
}
