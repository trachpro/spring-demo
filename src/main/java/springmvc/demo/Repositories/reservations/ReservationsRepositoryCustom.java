package springmvc.demo.Repositories.reservations;

import org.bson.Document;
import springmvc.demo.models.Reservation;

import java.util.Date;
import java.util.List;

public interface ReservationsRepositoryCustom {

    public List<Reservation> findReservationsBetweenDate(Date from, Date to);

}
